import {
  getToken, setToken, removeToken,
  getHost, setHost, removeHost,
  getUsername, setUsername, removeUsername,
  getPassword, setPassword, removePassword,
  getRole, setRole, removeRole
} from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  host: getHost(),
  username: getUsername(),
  password: getPassword(),
  role: getRole(),
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_HOST: (state, host) => {
    state.host = host
  },
  SET_USERNAME: (state, username) => {
    state.username = username
  },
  SET_PASSWORD: (state, password) => {
    state.password = password
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, host } = userInfo
    commit('SET_TOKEN', 'admin-token')
    commit('SET_HOST', host)
    commit('SET_USERNAME', username)
    commit('SET_PASSWORD', password)
    setToken('admin-token')
    setHost(host)
    setUsername(username)
    setPassword(password)
    setRole('admin')
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      const roles = [state.role]
      commit('SET_ROLES', roles)
      resolve({ roles: roles })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_HOST', '')
      commit('SET_USERNAME', '')
      commit('SET_PASSWORD', '')
      removeHost()
      removeUsername()
      removePassword()
      removeRole()
      removeToken()
      resetRouter()
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // Dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const HostKey = 'host'
const UsernameKey = 'username'
const PasswordKey = 'password'
const RoleKey = 'role'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getHost() {
  return Cookies.get(HostKey)
}

export function setHost(host) {
  return Cookies.set(HostKey, host)
}

export function removeHost() {
  return Cookies.remove(HostKey)
}

export function getUsername() {
  return Cookies.get(UsernameKey)
}

export function setUsername(username) {
  return Cookies.set(UsernameKey, username)
}

export function removeUsername() {
  return Cookies.remove(UsernameKey)
}

export function getPassword() {
  return Cookies.get(PasswordKey)
}

export function setPassword(password) {
  return Cookies.set(PasswordKey, password)
}

export function removePassword() {
  return Cookies.remove(PasswordKey)
}

export function getRole() {
  return Cookies.get(RoleKey)
}

export function setRole(role) {
  return Cookies.set(RoleKey, role)
}

export function removeRole() {
  return Cookies.remove(RoleKey)
}

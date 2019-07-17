/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const dbRouter = {
  path: '/db',
  component: Layout,
  redirect: 'noredirect',
  name: 'DB',
  meta: {
    title: 'db',
    icon: 'chart'
  },
  children: [
    {
      path: 'list',
      component: () => import('@/views/db/list'),
      name: 'DBList',
      meta: { title: 'DBList', noCache: true }
    },
    {
      path: 'new',
      component: () => import('@/views/db/new'),
      name: 'NewDB',
      meta: { title: 'NewDB', noCache: true }
    },
    {
      path: 'tables',
      component: () => import('@/views/db/tables'),
      name: 'Tables',
      hidden: true,
      meta: { title: 'Tables', noCache: true }
    },
    {
      path: 'tableDetail',
      component: () => import('@/views/db/table-detail'),
      name: 'TableDetail',
      hidden: true,
      meta: { title: 'TableDetail', noCache: true }
    },
    {
      path: 'newTable',
      component: () => import('@/views/db/new-table'),
      name: 'CreateTable',
      hidden: true,
      meta: { title: 'CreateTable', noCache: true }
    }
  ]
}

export default dbRouter

import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import echarts from './views/charts/echarts.vue'
import log from './views/log/log.vue'
import user from './views/user/user.vue'
import article from './views/article/article.vue'
import articleType from './views/article/articleType.vue'
import friendAddress from './views/article/friendAddress.vue'
import paramManager from './views/article/paramManager.vue'
import userPage from "./views/user/userPage.vue";

let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        name: '首页',
        leaf: true,//只有一个节点
        iconCls: 'el-icon-s-home',
        children: [
            { path: '/echarts', component: echarts, name: '首页'}
        ]
    },
    {
        path: '/',
        component: Home,
        name: '运维管理',
        iconCls: 'el-icon-monitor',//图标样式class
        children: [
            { path: '/log', component: log, name: '操作日志'},
        ]
    },
    {
        path: '/',
        component: Home,
        name: '行政管理',
        iconCls: 'el-icon-monitor',//图标样式class
        children: [
            { path: '/user', component: user, name: '用户管理'},
            { path:'/user2',component: userPage,name:'我的用户管理'},
        ]
    },
    {
        path: '/',
        component: Home,
        name: '博客管理',
        iconCls: 'el-icon-monitor',//图标样式class
        children: [
            { path: '/article', component: article, name: '文章管理'},
            { path: '/articleType', component: articleType, name: '文章类型管理'},
            { path: '/friendAddress', component: friendAddress, name: '友情链接管理'},
            { path: '/paramManager', component: paramManager, name: '参数管理'},
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;

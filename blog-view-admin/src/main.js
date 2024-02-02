import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
import routes from './routes'
import 'font-awesome/css/font-awesome.min.css'
import axios from 'axios'


//配置axios的全局基本路径
axios.defaults.baseURL='http://localhost:8080';
//全局属性配置，在任意组件内可以使用this.$http获取axios对象
Vue.prototype.$http = axios

Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)

const router = new VueRouter({
  routes
})

// request拦截器
/*axios.interceptors.request.use(config => {
  if (localStorage.getItem('U-TOKEN')) {
    // 让每个请求携带token--['X-Token']为自定义key 请根据实际情况自行修改
    config.headers['token'] = localStorage.getItem('U-TOKEN');
  }
  return config
}, error => {
  console.log('request拦截器 - 发生异常：');
  console.log(error);
  Promise.reject(error);
});*/

//axios的响应拦截器
/*axios.interceptors.response.use(res => {
  //6666是前后端约定好的错误码，
  if(res.data.code == 6666){
    //说明没有登录，
    router.push({ path: '/login' });
    ElementUI.Message({
      type: 'error',
      message: res.data.message
    });
    return Promise.reject(res.data.message);
  }
  return res;
}, err => {
  // 在请求错误时要做的事儿
  // 该返回的数据则是axios.catch(err)中接收的数据
  return Promise.reject(err)
});*/

//每次路由之前都要执行,每次请求都要经过路由
//每次请求都不拦截到
router.beforeEach((to, from, next) => {
  //NProgress.start();
  if (to.path == '/login') {
    //重新登录,把原来session移除掉
    localStorage.removeItem('realName');
    localStorage.removeItem('userPic');
  }
  next();
})

new Vue({
  router,
  store,
  render: h => h(App) // index.html id为app的div标签下面使用<App/>和template: '<App/>',一样的效果
}).$mount('#app') // 和el: '#app'效果一样都是挂载在index.html id为app的div标签上面


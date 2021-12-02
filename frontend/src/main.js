import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/en';
import '../theme/index.css'
//import 'element-ui/lib/theme-chalk/index.css';

import router from "@/router";
import "./axios";
import store from "@/vuex";
import fullCalendar from 'vue-fullcalendar'

Vue.component('full-calendar', fullCalendar)

Vue.use(ElementUI, {locale});
Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app')

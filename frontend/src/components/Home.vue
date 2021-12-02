<template>
  <div>
    <el-tabs type="border-card" v-if="token">
      <el-tab-pane label="Tasks"><Tasks/></el-tab-pane>
      <el-tab-pane label="Plan"><Plan/></el-tab-pane>
      <el-tab-pane label="Schedule"><Calendar/></el-tab-pane>
      <el-tab-pane label="Progress"><Progress/></el-tab-pane>
    </el-tabs>
    <h3 class="login" v-if="!token">You're not logged in. <router-link to="/login">Login</router-link></h3>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import Tasks from "@/components/Tasks/Tasks";
import Calendar from "@/components/Calendar/Calendar";
import Progress from "@/components/Progress/Progress";
import Plan from "@/components/Add plan/Plan";
import axios from "axios";

export default {
  name: "Home",
  computed: {
    ...mapGetters(["token"])
  },
  data(){
    return{
      tokens: []
    }
  },
  created() {
    let token = localStorage.getItem("token")
    this.getTokens().then(result=>{
      for (let i = 0; i < result.length; i++) {
        if (result[i].token === token) {
          this.$store.dispatch("token", token)
        }
      }
    })
  },
  components: {
    Tasks,
    Calendar,
    Progress,
    Plan
  },
  methods: {
    async getTokens() {
      try {
        const response = await axios.get("/tokens")
        this.tokens = response.data
        return response.data
      } catch (e) {
        console.log();
      }
    }
  }
}
</script>

<style scoped>
.login {
  padding: 50px;
}
</style>
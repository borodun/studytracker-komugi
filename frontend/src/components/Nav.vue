<template>
  <div>
    <el-menu mode="horizontal" @select="handleSelect" :default-active="activeIndex">
      <el-menu-item index="1">Home</el-menu-item>
      <el-menu-item index="2" v-if="!token">Login</el-menu-item>
      <el-menu-item index="3" v-if="!token">Register</el-menu-item>
      <el-menu-item index="4" v-if="token">Logout</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import {mapGetters} from "vuex"

export default {
  name: "Nav",
  data() {
    return {
      activeIndex: '1'
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
      switch (key) {
        case "1":
          this.$router.push("/")
          break
        case "2":
          this.$router.push("/login")
          break
        case "3":
          this.$router.push("/register")
          break
        case "4":
          localStorage.removeItem("token")
          this.$store.dispatch("token", null)
          this.$router.push("/")
          break
      }
    }
  },
  computed: {
    ...mapGetters(["token"])
  }
}
</script>

<style scoped>

</style>
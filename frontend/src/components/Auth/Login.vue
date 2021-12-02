<template>
  <div class="login">
    <h1>Login</h1>
    <el-form status-icon label-width="120px" @submit.prevent="handleSubmit">
      <el-form-item label="Login">
        <el-input v-model="login"></el-input>
      </el-form-item>
      <el-form-item label="Password" prop="pass">
        <el-input type="password" v-model="pass"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">Login</el-button>
        <el-button @click="forgot">Forgot password?</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Login",
  data() {
    return {
      login: "",
      pass: "",
      error: ""
    }
  },
  methods: {
    async handleSubmit() {
      try {
        const response = await axios.get("/users/login", {
          params: {
            login: this.login,
            pass: this.pass
          }
        })
        console.log(response.data)
        localStorage.setItem('token', response.data)
        await this.$store.dispatch("token", response.data)
        await this.$router.push("/")
      } catch (e) {
        console.log();
        this.error = "Invalid username or password"
      }
    },
    forgot() {
      this.$router.push("/forgot")
    }
  }
}
</script>

<style scoped>

h1 {
  text-align: center;
  position: center;
}

.login{
  width: 500px;
  padding: 20px;
  padding-right: 60px;
}
</style>
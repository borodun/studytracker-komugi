<template>
  <div class="reset">
    <h1>Change password</h1>
    <el-form status-icon label-width="60px" @submit.prevent="handleSubmit">
      <el-form-item label="Password" prop="password">
        <el-input type="password" v-model="password"></el-input>
      </el-form-item>
      <el-form-item label="Password confirm" prop="passwordConfirm">
        <el-input type="password" v-model="passwordConfirm"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">Change</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Reset",
  data() {
    return {
      login: "",
      password: "",
      token: "",
      passwordConfirm: ""
    }
  },
  methods: {
    async handleSubmit() {
      const response = await axios.put("/users", {
        params: {
          token: this.token
        },
        login: this.login,
        password: this.password,
        //passwordConfirm: this.passwordConfirm,
        //token: this.$route.params.token
      })

      console.log(response)
      await this.$router.push("/login")
    }
  }
}
</script>

<style scoped>
h1 {
  text-align: center;
  position: center;
}

.reset{
  width: 500px;
  padding: 20px;
  padding-right: 60px;
}
</style>
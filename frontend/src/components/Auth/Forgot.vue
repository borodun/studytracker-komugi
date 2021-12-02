<template>
  <div class="forgot">
    <h1>Restore password</h1>
    <el-form status-icon label-width="60px" @submit.prevent="handleSubmit">
      <el-form-item label="Email">
        <el-input v-model="email"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">Send email</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Forgot",
  data() {
    return {
      email: "",
      message: "",
      error: ""
    }
  },
  methods: {
    async handleSubmit() {
      try {
        await axios.post("forgot", {
          email: this.email
        })

        this.message = "The email was sent!"
        this.error = ""
      } catch (e) {
        this.error = "Error occurred!"
        this.message = ""
      }
    }
  }
}
</script>

<style scoped>
h1 {
  text-align: center;
  position: center;
}

.forgot{
  width: 500px;
  padding: 20px;
  padding-right: 60px;
}
</style>
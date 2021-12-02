<template>
  <div class="register">
    <h1>Register</h1>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" status-icon label-width="150px"
             @submit.prevent="submitForm('ruleForm')">
      <el-form-item label="Login" prop="login" required>
        <el-input v-model="ruleForm.login"></el-input>
      </el-form-item>
      <el-form-item label="Email" prop="email" required>
        <el-input v-model="ruleForm.email"></el-input>
      </el-form-item>
      <el-form-item label="Password" prop="password" required>
        <el-input type="password" v-model="ruleForm.password"></el-input>
      </el-form-item>
      <el-form-item label="Password confirm" prop="passwordConfirm" required>
        <el-input type="password" v-model="ruleForm.passwordConfirm"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">Register</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Register",
  data() {
    const validateLogin = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('Please input the login'));
          } else {
            this.checkLogin(value).then((result) => {
                  console.log(result)
                  if (result === false) {
                    callback(new Error('That login already in use'));
                    console.log(value + " in use")
                  } else {
                    callback();
                  }
                }
            )
          }
        }
    ;
    const validateEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the email'));
      } else {
        this.checkEmail(value).then((result) => {
              console.log(result)
              if (result === false) {
                callback(new Error('That login already in use'));
                console.log(value + " in use")
              } else {
                callback();
              }
            }
        )
      }
    };
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password'));
      } else {
        if (this.ruleForm.passwordConfirm !== '') {
          this.$refs.ruleForm.validateField('passwordConfirm');
        }
        callback();
      }
    };
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password again'));
      } else if (value !== this.ruleForm.password) {
        callback(new Error('Passwords don\'t match!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        login: "",
        password: '',
        passwordConfirm: '',
        email: "",
        age: ''
      },
      rules: {
        login: [
          {validator: validateLogin, trigger: 'change'}
        ],
        email: [
          {validator: validateEmail, trigger: 'change'}
        ],
        password: [
          {validator: validatePass, trigger: 'change'}
        ],
        passwordConfirm: [
          {validator: validatePass2, trigger: 'change'}
        ],

      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.register()
        } else {
          console.log('error submit!!');
          return false;
        }
      })
      ;
    },
    async register() {
      if (this.password !== this.passwordConfirm) {
        return;
      }
      try {
        const response = await axios.post("/users", {
          login: this.ruleForm.login,
          password: this.ruleForm.password,
          email: this.ruleForm.email
        })

        console.log(response)
        await this.$router.push('/login')
      } catch (e) {
        this.error = "Error occurred!"
      }
    }
    ,
    async checkLogin(login) {
      try {
        const response = await axios.get("/users/login/" + login)
        console.log(response)
        console.log(response.data === "")
        return response.data === "";
      } catch (e) {
        this.error = "Error occurred!"
      }
    }
    ,
    async checkEmail(email) {
      try {
        const response = await axios.get("/users/email/" + email)
        return response.data === "";
      } catch (e) {
        this.error = "Error occurred!"
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

.register {
  width: 500px;
  padding: 20px;
  padding-right: 60px;
}
</style>
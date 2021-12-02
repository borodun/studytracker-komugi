<template>
  <div class="prog" v-on:click="getProgress">
    <h1>Progress</h1>
    <el-form status-icon label-width="120px">
      <el-form-item
          v-for="prog in progress"
          v-bind:key="prog.name"
          :label="prog.name">
        <el-progress :text-inside="true" :stroke-width="26" :percentage="prog.value"></el-progress>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
  name: "Progress",
  computed: {
    ...mapGetters(["token"])
  },
  data() {
    return {
      progress: []
    }
  },
  created() {
    this.getProgress()
  },
  methods: {
    async getProgress() {
      this.progress = []
      try {
        const response = await axios.get("/subjects/user", {
          params: {
            token: this.token,
          }
        })
        const progMap = new Map();
        for (let i = 0; i < response.data.length; i++) {
          let key = response.data[i].name
          progMap.set(key, progMap.get(key) + 1 || 1)
        }

        progMap.forEach((value, key) => {
          console.log(key)
          let prog = {}
          prog.name = key
          prog.value = value / 8 * 100
          this.progress.push(prog)
        })

      } catch (e) {
        console.log(e);
      }
    }
  }
}
</script>

<style scoped>
.prog {
  width: 900px;
  height: 600px;
  text-align: center;
  margin-left: 1rem;
  margin-right: 1rem;
  margin-top: 10px;
}
</style>
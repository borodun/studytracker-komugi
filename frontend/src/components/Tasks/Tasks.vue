<template>
  <div class="main container" v-on:click="start">
    <h1>Tasks</h1>
    <div v-if="isEmpty">
      <el-alert
          title="There are no tasks at this moment"
          type="info"
          :center="true"
          show-icon
          :closable="false">
      </el-alert>
    </div>

    <div v-if="isWrongActivity" class="wrongNotification">
      <el-alert
          :title="errorMessage"
          type="error"
          :center="true"
          show-icon
          :closable="false">
      </el-alert>
    </div>

    <Activity v-if="!isEmpty"></Activity>
    <InformationSection></InformationSection>
    <InputSection></InputSection>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import Activity from './Activity.vue';
import InformationSection from './InformationSection.vue';
import InputSection from './InputSection.vue';
import axios from "axios";
export default {
  name: 'Tasks',
  components: {
    Activity,
    InformationSection,
    InputSection,
  },
  created() {
   this.start()
  },
  computed: {
    ...mapGetters({
      activities: 'getActivities',
      errorMessage: 'getErrorMessage',
      token: "token"
    }),
    isEmpty() {
      return this.activities.length === 0;
    },
    isWrongActivity() {
      return this.errorMessage.length > 0;
    },
  },
  methods: {
    ...mapActions(['addActivity', 'deleteActivity']),
    start(){
      let oldActivities = this.activities
      for (let i = 0; i < oldActivities.length; i++) {
        console.log("i: " + i)
        console.log({ activity: oldActivities[i] })
        this.deleteActivity({ activity: oldActivities[i] })
      }
      this.downloadTasks()
    },
    async downloadTasks() {
      try {
        const response = await axios.get("/tasks/user", {
          params: {
            token: this.token,
          }
        })
        for (const i in response.data) {
          let task = response.data[i]
          const activity = {
            name: task.name,
            completed: false,
            date: task.description,
            id: task.id,
          };
          this.addActivity({activity});
        }
        console.log("activities")
        console.log(this.activities)
      } catch (e) {
        console.log(e);
      }
    }
  }
};
</script>

<style>
.main {
  width: 900px;
  height: 600px;
  text-align: center;
  margin-left: 1rem;
  margin-right: 1rem;
  margin-top: 10px;
}
.wrongNotification {
  margin-bottom: 0.2rem;
  width: 80%;
  margin-left: 10%;
  text-align: center;
  margin-top: 10px;
}
</style>
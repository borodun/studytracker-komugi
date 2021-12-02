<template>
  <div>
    <el-row>
      <el-col :span="3">
        Name:
      </el-col>

      <el-col :span="8">
        <el-input
            placeholder="Please input the task name"
            v-model="activity"
            size="mini">
        </el-input>
      </el-col>
      <el-col :span="3">
        Deadline:
      </el-col>
      <el-col :span="8">
        <el-date-picker
            v-model="date"
            type="date"
            placeholder="Pick a day">
        </el-date-picker>
      </el-col>
    </el-row>
    <el-button
        type="primary"
        icon="el-icon-circle-plus-outline"
        circle
        @click="addActivityMethod()">
    </el-button>
  </div>
</template>

<script>
import {mapActions, mapGetters} from 'vuex';
import axios from "axios";

export default {
  name: 'InputSection',
  data() {
    return {
      activity: '',
      date: '',
    };
  },
  computed: {
    ...mapGetters({
      activities: 'getActivities',
      errorMessage: 'getErrorMessage',
      token: "token"
    })
  },
  methods: {
    ...mapActions(['addActivity', 'setErrorMessageAction']),
    maxId() {
      let maxId = 0
      for (let i = 0; i < this.activities.length; i++) {
        if (this.activities[i].id > maxId) {
          maxId = this.activities[i].id
        }
      }
      console.log("max id: " + maxId)
      return maxId + 1
    },
    addActivityMethod() {
      if (this.validateData() === true) {
        const activity = {
          name: this.activity,
          completed: false,
          date: this.date,
          id: this.maxId(),
        };
        this.setErrorMessageAction({message: ''});
        this.addActivity({activity});
        this.addTaskToUser(activity)
        this.activity = '';
        this.date = '';
      } else {
        this.setMessageError();
      }
    },
    async addTaskToUser(task) {
      try {
        await axios.post("/tasks?token=" + this.token, {
          name: task.name,
          description: task.date,
          subjectId: 1,
          tag: ""
        })
      } catch (e) {
        console.log(e);
      }
    },
    validateData() {
      return this.activity !== '' && this.date !== '';
    },
    setMessageError() {
      let error = '';
      if (this.activity === '' && this.date === '') {
        error = 'The activity && the date are empty';
      } else if (this.activity === '') {
        error = 'The activity is empty';
      } else {
        error = 'The date is empty';
      }
      this.setErrorMessageAction({message: error});
    },
  },
};
</script>

<style>
</style>
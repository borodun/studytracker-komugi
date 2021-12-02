<template>
  <div class="main" v-on:click="start">
    <h1>Schedule</h1>
    <fullCalendar  :events="events" ></fullCalendar>
  </div>
</template>

<script>
import fullCalendar from "vue-fullcalendar";
import axios from "axios";
import {mapGetters} from "vuex";

export default {
  name: "Calendar",
  components: {
    fullCalendar
  },
  computed: {
    ...mapGetters(["token"])
  },
  data() {
    return {
      events: []
    }
  },
  mounted() {
    this.start()
  },
  methods: {
    start() {
      this.events = []
      this.getSchedule()
      this.getTasks()
    },
    async getSchedule() {
      try {
        const response = await axios.get("/subjects/user", {
          params: {
            token: this.token,
          }
        })
        for (let i = 0; i < response.data.length; i++) {
          let event = {}
          event.title = response.data[i].name
          event.start = response.data[i].type
          this.events.push(event)
        }
      } catch (e) {
        console.log(e);
      }
    },
    async getTasks(){
      try {
        const response = await axios.get("/tasks/user", {
          params: {
            token: this.token,
          }
        })
        for (let i = 0; i < response.data.length; i++) {
          let event = {}
          event.title = response.data[i].name
          event.start = response.data[i].description
          this.events.push(event)
        }
      } catch (e) {
        console.log(e);
      }
    }
  }
}
</script>

<style scoped>
.main {
  width: 900px;
  height: 600px;
  text-align: center;
  margin-left: 1rem;
  margin-right: 1rem;
  margin-top: 10px;
}
</style>
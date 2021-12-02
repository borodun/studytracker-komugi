<template>
  <div class="plan">
    <el-row :gutter="20">
      <el-col :span="12">
        <h1>Make plan</h1>
        <el-form :model="form" ref="form" label-width="120px">
          <el-form-item label="Plan name" prop="Name" required>
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="Plan time" required>
            <el-form-item prop="Date">
              <el-date-picker type="date" placeholder="Pick a date" v-model="form.date" style="width: 100%;"/>
            </el-form-item>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="createPlan()">Create</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <div class="grid-content bg-purple">
          <h1>Delete plan</h1>
          <el-form label-width="120px">
            <el-form-item label="Plan name" prop="name">
              <el-select v-model="selectedPlanId" placeholder="Select plan" v-on:focus="getPlans()">
                <el-option
                    v-for="plan in plans"
                    :key="plan.id"
                    :label="plan.name"
                    :value="plan.id">
                  <span style="float: left">{{ plan.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ plan.date }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="openModal()">Delete</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
  name: "Plan",
  computed: {
    ...mapGetters(["token"])
  },
  data() {
    return {
      form: {
        name: '',
        date: '',
      },
      plans: [],
      selectedPlanId: null
    }
  },
  methods: {
    async createPlan() {
      if (this.form.name === "" || this.form.date === "") {
        return
      }
      try {
        await axios.post("/subjects?token=" + this.token, {
          name: this.form.name,
          dayOfWeek: 0,
          timeOfDay: "10:00",
          classroom: "0000",
          type: this.form.date
        })
        this.$message({
          type: 'success',
          message: 'Plan created',
        });
      } catch (e) {
        console.log(e);
      }
    },
    async getPlans() {
      this.plans = []
      try {
        const response = await axios.get("/subjects/user", {
          params: {
            token: this.token,
          }
        })
        for (let i = 0; i < response.data.length; i++) {
          let plan = {}
          plan.name = response.data[i].name
          plan.id = response.data[i].id
          plan.date = response.data[i].type.split("T")[0]
          this.plans.push(plan)
        }
      } catch (e) {
        console.log(e);
      }
    },
    async deletePlan() {
      if (this.selectedPlanId == null) {
        return
      }
      try {
        await axios.delete("/subjects", {
          params: {
            token: this.token,
            id: this.selectedPlanId
          }
        })
      } catch (e) {
        console.log(e);
      }
    },
    openModal() {
      const message = `This will permanently delete the plan. Continue?`;
      this.$confirm(message, 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }).then(() => {
        this.deletePlan()
            .then(() => {
              this.$message({
                type: 'success',
                message: 'Delete completed',
              });
            });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Delete canceled',
        });
      });
    }
  }
}
</script>

<style scoped>
.plan {
  width: 900px;
  height: 600px;
  margin-left: 1rem;
  margin-right: 1rem;
  margin-top: 10px;
}
h1{
  text-align: center;
}
</style>
<template>
  <div>
    <el-table
        :data="activities"
        :row-class-name="tableRowColor">
      <el-table-column
          prop="name"
          label="Name">
      </el-table-column>
      <el-table-column
          label="Date">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{getDate(scope.row.date)}}</span>
        </template>
      </el-table-column>
      <el-table-column
          label="Remove">
        <template slot-scope="scope">
          <el-button type="danger"
                     icon="el-icon-delete"
                     @click="openModal(scope.row)"
                     circle
                     size="small">
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
          label="Check">
        <template slot-scope="scope">
          <el-button
              type="success"
              icon="el-icon-check"
              circle @click="completeActivity(scope.row)"
              size="small">
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import moment from 'moment';
import axios from "axios";
export default {
  name: 'Activity',
  data() {
    return {
      activity: '',
      date: '',
    };
  },
  computed: {
    ...mapGetters({
      token: "token",
      activities: 'getActivities',
    }),
  },
  methods: {
    ...mapActions(['addActivity', 'deleteActivity', 'changeActivityState']),
    completeActivity(item) {
      this.changeActivityState({ activity: item });
    },
    tableRowColor({ row }) {
      if (row.completed === true) {
        return 'success-row';
      }
      return 'warning-row';
    },
    getDate(item) {
      return moment(item).format('DD/MM/YYYY');
    },
    openModal(item) {
      const message = `This will permanently delete the activity ${item.name}. Continue?`;
      this.$confirm(message, 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }).then(() => {
        this.deleteActivity({ activity: item })
            .then(() => {
              this.$message({
                type: 'success',
                message: 'Delete completed',
              });
            });
        this.deleteFromDB(item)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Delete canceled',
        });
      });
    },
    async deleteFromDB(task) {
      try {
        await axios.delete("/tasks", {
          params: {
            token: this.token,
            id: task.id
          }
        })
      } catch (e) {
        console.log(e);
      }
    }
  },
};
</script>

<style>
</style>
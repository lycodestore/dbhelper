<template>
  <div>
    <el-table
      border
      :data="tableData"
    >
      <el-table-column
        v-for="(item, index) in header"
        :key="index"
        align="center"
        :label="item"
      >
        <template slot-scope="scope">
          <el-input v-model="scope.row[item]" />
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center; margin-top: 20px;">
      <el-button type="primary" @click="submitModify">提交修改</el-button>
    </div>
  </div>
</template>
<style scoped>
.add-column {
  color: rgb(24, 144, 255);
  font-size: 40px;
  cursor: pointer;
}
</style>
<script>
export default {
  props: {
    header: {
      type: Array,
      default() {
        return []
      }
    },
    oldRecord: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      tableData: [],
      newRecord: {}
    }
  },
  mounted() {
    this.newRecord = Object.assign({}, this.oldRecord)
    this.tableData.push(this.newRecord)
  },
  methods: {
    submitModify() {
      this.$emit('confirmModify', {
        newRecord: this.tableData[0],
        oldRecord: this.oldRecord
      })
    }
  }
}
</script>

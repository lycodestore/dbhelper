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
      <el-table-column
        label="操作"
        align="center"
      >
        <template slot-scope="scope">
          <el-button type="danger" size="small" plain @click="deleteRow(scope.row.index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px;">
      <i class="el-icon-circle-plus add-column" @click="newRow" />
    </div>
    <div style="text-align: center;">
      <el-button type="primary" @click="confirmAdd">确认新增</el-button>
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
    }
  },
  data() {
    return {
      tableData: [],
      currentIndex: 0
    }
  },
  methods: {
    newRow() {
      const row = {}
      this.header.forEach(ele => {
        row[ele] = null
      })
      row.index = this.currentIndex
      this.currentIndex++
      this.tableData.push(row)
    },
    deleteRow(index) {
      this.tableData.forEach(ele => {
        if (ele.index === index) {
          const deleteIndex = this.tableData.indexOf(ele)
          this.tableData.splice(deleteIndex, 1)
        }
      })
    },
    confirmAdd() {
      this.$emit('confirmInsert', this.tableData)
    }
  }
}
</script>

<template>
  <div class="content-wrapper">
    <el-card shadow="always">
      <div slot="header">
        新建数据表
      </div>
      <el-form label-width="90px">
        <el-form-item label="数据表名称">
          <el-input v-model="tablename" />
        </el-form-item>
      </el-form>
      <el-table
        border
        :data="tableData"
      >
        <el-table-column
          prop="column"
          label="列名"
          align="center"
        >
          <template slot-scope="scope">
            <el-input v-model="scope.row.column" />
          </template>
        </el-table-column>
        <el-table-column
          prop="type"
          label="数据类型"
          align="center"
        >
          <template slot-scope="scope">
            <el-select v-model="scope.row.type" placeholder="数据类型">
              <el-option
                v-for="(item, index) in typeList"
                :key="index"
                :label="item"
                :value="item"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          prop="length"
          label="长度"
          align="center"
        >
          <template slot-scope="scope">
            <el-input v-model.number="scope.row.length" />
          </template>
        </el-table-column>
        <el-table-column
          prop="default"
          label="默认值"
          align="center"
        >
          <template slot-scope="scope">
            <el-input v-model.number="scope.row.default" />
          </template>
        </el-table-column>
        <el-table-column
          prop="isKey"
          label="设为主键"
          align="center"
        >
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.isKey">是</el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          prop="comment"
          label="备注"
          align="center"
        >
          <template slot-scope="scope">
            <el-input v-model.number="scope.row.comment" />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="danger" size="small" @click="deleteColumn(scope.row.index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px;">
        <i class="el-icon-circle-plus add-column" @click="addColumn" />
      </div>
      <div style="text-align: center;">
        <el-button type="primary" @click="createTable">
          新建数据表
        </el-button>
      </div>
    </el-card>
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
  data() {
    return {
      tablename: '',
      tableData: [
        {
          index: 0,
          column: '',
          type: '',
          length: 0,
          default: '',
          isKey: false,
          comment: ''
        }
      ],
      currentIndex: 0,
      typeList: [
        'BIGINT',
        'BINARY',
        'BIT',
        'BLOB',
        'BLOB DATA TYPE',
        'BOOLEAN',
        'CHAR',
        'CHAR BYTE',
        'DATE',
        'DATETIME',
        'DEC',
        'DECIMAL',
        'DOUBLE',
        'DOUBLE PRECISION',
        'ENUM',
        'FLOAT',
        'INT',
        'INTEGER',
        'LONGBLOB',
        'LONGTEXT',
        'MEDIUMBLOB',
        'MEDIUMINT',
        'MEDIUMTEXT',
        'SET DATA TYPE',
        'SMALLINT',
        'TEXT',
        'TIME',
        'TIMESTAMP',
        'TINYBLOB',
        'TINYINT',
        'TINYTEXT',
        'VARBINARY',
        'VARCHAR',
        'YEAR DATA TYPE'
      ]
    }
  },
  methods: {
    addColumn() {
      this.currentIndex++
      this.tableData.push({
        index: this.currentIndex,
        column: '',
        type: '',
        length: 0,
        default: '',
        isKey: false,
        comment: ''
      })
    },
    deleteColumn(index) {
      this.tableData.forEach(ele => {
        if (ele.index === index) {
          const delIndex = this.tableData.indexOf(ele)
          this.tableData.splice(delIndex, 1)
        }
      })
    },
    createTable() {
      this.$http.post('/new/table', {
        dbname: this.$route.query.dbname,
        tablename: this.tablename,
        tableData: this.tableData
      }).then(res => {
        if (res.statusCode === 20000) {
          this.$router.push({
            path: '/db/tables',
            query: {
              dbname: this.$route.query.dbname
            }
          })
        } else {
          this.$message({
            showClose: true,
            message: res.statusMsg,
            type: 'error'
          })
        }
      }).catch(err => {
        console.error(err)
      })
    }
  }
}
</script>

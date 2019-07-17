<template>
  <div class="tables-wrapper">
    <el-card shadow="always">
      <div slot="header" style="overflow: hidden;">
        <span style="float: left;">数据表</span>
        <el-button
          type="primary"
          size="small"
          style="float: right;"
          @click="createTable"
        >
          新建数据表
        </el-button>
        <el-button
          type="primary"
          size="small"
          style="float: right; margin-right: 20px;"
          @click="showSql"
        >
          查看sql
        </el-button>
      </div>
      <el-table
        :data="tables"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="name"
          label="数据表名称"
          align="center"
        />
        <el-table-column
          label="操作"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="success" size="small" plain @click="handleCheck(scope.row.name)">查看</el-button>
            <el-button type="danger" size="small" plain @click="deleteTable(scope.row.name)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!--查看sql弹窗-->
    <el-dialog
      title="建库语句"
      :visible.sync="showSqlFrame"
      center
      :close-on-click-modal="false"
    >
      <textarea
        spellcheck="false"
        autocapitalize="off"
        autocomplete="off"
        autocorrect="off"
        v-model="sql"
        class="sql-frame"
        cols="30"
        rows="10"
      />
    </el-dialog>
  </div>
</template>
<style scoped>
.tables-wrapper {
  margin: 30px 80px 30px 80px;
}

.sql-frame {
  width: 100%;
  padding: 10px;
  resize: none;
  outline: none;
  border: 1px solid #ccc;
  border-radius: 10px;
}
</style>

<script>
export default {
  data() {
    return {
      tables: [],
      showSqlFrame: false,
      sql: ''
    }
  },
  created() {
    this.$http.get('/check/tables/' + this.$route.query.dbname).then(res => {
      this.tables = res
    }).catch(err => {
      console.error(err)
    })
  },
  methods: {
    handleCheck(tablename) {
      const dbname = this.$route.query.dbname
      this.$router.push({
        path: '/db/tableDetail',
        query: {
          dbname: dbname,
          tablename: tablename
        }
      })
    },
    createTable() {
      this.$router.push({
        path: '/db/newTable',
        query: {
          dbname: this.$route.query.dbname
        }
      })
    },
    deleteTable(tablename) {
      this.$confirm(`是否确定删除数据表${tablename}`, '提示', {
        confirmButtonText: '确定',
        type: 'warning'
      }).then(() => {
        this.$http.get('/delete/table/' + this.$route.query.dbname + '/' + tablename)
          .then(res => {
            this.$http.get('/check/tables/' + this.$route.query.dbname).then(res => {
              this.tables = res
            }).catch(err => {
              console.error(err)
            })
          })
      }).catch(() => {
      })
    },
    showSql() {
      const dbname = this.$route.query.dbname
      this.$http.get('/check/showcreatedb/' + dbname).then(res => {
        this.sql = res
        this.showSqlFrame = true
      }).catch(err => {
        console.error(err)
      })
    }
  }
}
</script>

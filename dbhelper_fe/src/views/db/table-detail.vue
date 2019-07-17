<template>
  <div class="content-wrapper">
    <el-card shadow="always">
      <el-card shadow="always">
        <div slot="header" style="overflow: hidden;">
          <span>{{ tablename }}</span>
          <el-button
            type="primary"
            style="float: right;"
            @click="showSql"
          >
            查看sql
          </el-button>
        </div>
        <el-table
          :data="structure"
          border
        >
          <el-table-column
            prop="Field"
            label="Field"
            align="center"
          />
          <el-table-column
            prop="Type"
            label="Type"
            align="center"
          />
          <el-table-column
            prop="Key"
            label="Key"
            align="center"
          />
          <el-table-column
            prop="Default"
            label="Default"
            align="center"
          />
          <el-table-column
            prop="Extra"
            label="Extra"
            align="center"
          />
          <el-table-column
            prop="Privileges"
            label="Privileges"
            align="center"
          />
          <el-table-column
            prop="Comment"
            label="Comment"
            align="center"
          />
        </el-table>
      </el-card>
      <el-card shadow="always" style="margin-top: 20px;">
        <div slot="header" style="overflow: hidden;">
          <span>数据</span>
          <el-button
            type="primary"
            size="small"
            style="float: right;"
            @click="downloadToExcel"
            :loading="downloadLoading"
            icon="document"
          >
            下载所有数据到excel
          </el-button>
          <el-button
            type="primary"
            size="small"
            style="float: right; margin-right: 20px;"
            @click="insertRecord"
          >
            新增数据
          </el-button>
        </div>
        <el-table
          :data="tableData"
          border
        >
          <el-table-column
            v-for="(item, index) in header"
            :key="index"
            :prop="item"
            :label="item"
            align="center"
          />
          <el-table-column
            label="操作"
            width="150"
            align="center"
          >
            <template slot-scope="scope">
              <el-button type="primary" size="small" plain @click="handleEdit(scope.row)">修改</el-button>
              <el-button type="danger" size="small" plain @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 10px; text-align: center">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            @current-change="changePage"
          />
        </div>
      </el-card>
    </el-card>
    <!-- 新增数据的弹窗-->
    <el-dialog title="新增记录" :visible.sync="showInsertFrame" center :close-on-click-modal="false">
      <insert-frame :header="header" @confirmInsert="submitInsert" />
    </el-dialog>
    <!-- 修改记录的弹窗-->
    <el-dialog v-if="showEditFrame" title="修改记录" :visible.sync="showEditFrame" center :close-on-click-modal="false">
      <edit-frame
        :header="header"
        :old-record="currentRecord"
        @confirmModify="submitModify"
      />
    </el-dialog>
    <!-- 查看sql的弹窗-->
    <el-dialog title="建表语句" :visible.sync="showSqlFrame" center :close-on-click-modal="false">
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
import InsertFrame from './components/insert-frame'
import EditFrame from './components/edit-frame'

export default {
  components: {
    InsertFrame,
    EditFrame
  },
  data() {
    return {
      structure: [],
      tablename: '',
      header: [],
      tableData: [],
      total: 0,
      showInsertFrame: false,
      currentPage: 1,
      showEditFrame: false,
      currentRecord: {},
      showSqlFrame: false,
      sql: '',
      downloadLoading: false
    }
  },
  created() {
    const dbname = this.$route.query.dbname
    const tablename = this.$route.query.tablename
    this.$http.get('/check/' + dbname + '/' + tablename).then(res => {
      this.tablename = res.name
      this.structure = res.infos
    }).catch(err => {
      console.error(err)
    })

    this.$http.post('/check/data', {
      dbname: this.$route.query.dbname,
      tablename: this.$route.query.tablename,
      offset: 0
    }).then(res => {
      this.total = res.total
      this.header = res.header
      this.tableData = res.data
    }).catch(err => {
      console.error(err)
    })
  },
  methods: {
    changePage(page) {
      this.currentPage = page
      this.$http.post('/check/data', {
        dbname: this.$route.query.dbname,
        tablename: this.$route.query.tablename,
        offset: (page - 1) * 10
      }).then(res => {
        this.total = res.total
        this.header = res.header
        this.tableData = res.data
      }).catch(err => {
        console.error(err)
      })
    },
    handleEdit(row) {
      this.currentRecord = row
      this.showEditFrame = true
    },
    insertRecord() {
      this.showInsertFrame = true
    },
    submitInsert(tableData) {
      tableData.map(ele => {
        delete ele.index
      })
      const dbname = this.$route.query.dbname
      const tablename = this.$route.query.tablename
      this.$http.post('/new/record', {
        dbname: dbname,
        tablename: tablename,
        records: tableData
      }).then(res => {
        if (res.statusCode === 20000) {
          this.showInsertFrame = false
          this.$http.post('/check/data', {
            dbname: this.$route.query.dbname,
            tablename: this.$route.query.tablename,
            offset: 0
          }).then(res => {
            this.total = res.total
            this.header = res.header
            this.tableData = res.data
          }).catch(err => {
            console.error(err)
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
    },
    handleDelete(row) {
      this.$http.post('/delete/record', {
        dbname: this.$route.query.dbname,
        tablename: this.$route.query.tablename,
        record: row
      }).then(res => {
        if (res.statusCode === 20000) {
          this.$http.post('/check/data', {
            dbname: this.$route.query.dbname,
            tablename: this.$route.query.tablename,
            offset: 0
          }).then(res => {
            if (res.total > (this.currentPage - 1) * 10) {
              this.changePage(this.currentPage)
            } else {
              if (this.currentPage === 1) {
                this.changePage(1)
              } else {
                this.changePage(this.currentPage - 1)
              }
            }
          }).catch(err => {
            console.error(err)
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
    },
    submitModify(record) {
      this.$http.post('/update/record', {
        dbname: this.$route.query.dbname,
        tablename: this.$route.query.tablename,
        oldRecord: record.oldRecord,
        newRecord: record.newRecord
      }).then(res => {
        if (res.statusCode === 20000) {
          this.showEditFrame = false
          this.changePage(this.currentPage)
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
    },
    showSql() {
      const dbname = this.$route.query.dbname
      const tablename = this.$route.query.tablename
      this.$http.get('/check/showsql/' + dbname + '/' + tablename).then(res => {
        this.sql = res
        this.showSqlFrame = true
      }).catch(err => {
        console.error(err)
      })
    },
    downloadToExcel() {
      this.$http.post('/check/alldata', {
        dbname: this.$route.query.dbname,
        tablename: this.$route.query.tablename
      }).then(res => {
        console.log(res)
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = res.header
          const filterVal = res.header
          const list = res.data
          const data = this.formatJson(filterVal, list)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: this.$route.query.tablename,
            autoWidth: true,
            bookType: 'xlsx'
          })
          this.downloadLoading = false
        })
        }).catch(err => {
          console.error(err)
        })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>

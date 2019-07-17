<template>
  <el-row :gutter="40" class="panel-group">
    <el-col v-for="(item, index) in dblist" :key="index" :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div
          class="card-panel-icon-wrapper icon-db"
          @click="handleDbClicked(item.name)"
        >
          <svg-icon icon-class="documentation" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{ item.name }}
          </div>
          <div>
            <el-button type="primary" size="small">修改</el-button>
            <el-button type="danger" size="small" @click="deleteDatabase(item.name)">删除</el-button>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data() {
    return {
      dblist: [],
      forbidList: [
        'information_schema',
        'performance_schema',
        'mysql'
      ]
    }
  },
  created() {
    this.$http.get('/check/dblist').then(res => {
      this.dblist = res
    }).catch(err => {
      console.error(err)
    })
  },
  methods: {
    handleDbClicked(dbname) {
      this.$router.push({
        path: '/db/tables',
        query: {
          dbname: dbname
        }
      })
    },
    deleteDatabase(dbname) {
      this.$confirm(`确定删除数据库${dbname}?`, '确认', {
        confirmButtonText: '确定',
        type: 'warning'
      }).then(() => {
        this.$http.get('/delete/database/' + dbname).then(res => {
          if (res.statusCode === 20000) {
            this.$confirm('删除数据库成功！', '提示', {
              confirmButtonText: '确定',
              type: 'success'
            }).then(() => {
              this.$http.get('/check/dblist').then(res => {
                this.dblist = res
              }).catch(err => {
                console.error(err)
              })
            }).catch(() => {
            })
          } else {
            this.$message({
              showClose: true,
              message: res.statusMsg,
              type: 'error'
            })
          }
        })
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-db {
         background: #36a3f7;
      }
    }
    .icon-db {
      color: #36a3f7;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
      cursor: pointer;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
    }
  }
}
</style>

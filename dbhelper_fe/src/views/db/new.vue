<template>
  <div class="content-wrapper">
    <el-card shadow="always">
      <el-row>
        <el-col :offset="8" :span="8">
          <el-card shadow="always">
            <div slot="header">
              新建数据库
            </div>
            <el-form label-width="90px">
              <el-form-item label="数据库名称">
                <el-input v-model="dbname" />
              </el-form-item>
              <el-form-item label="字符集">
                <el-select
                  v-model="characterset"
                  placeholder="请选择数据库默认字符集"
                  @change="charactersetChange"
                >
                  <el-option
                    v-for="(item, index) in charactersetList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="校验集">
                <el-select v-model="collation" placeholder="请选择数据库默认校验集">
                  <el-option
                    v-for="(item, index) in collationList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-form>
            <div style="margin-top: 20px; text-align: center;">
              <el-button
                type="primary"
                @click="createDatabase"
              >
                创建数据库
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dbname: '',
      characterset: '',
      collation: '',
      charactersetList: [],
      collationList: []
    }
  },
  created() {
    this.$http.get('/helper/characterset').then(res => {
      this.charactersetList = res
    }).catch(err => {
      console.error(err)
    })
  },
  methods: {
    charactersetChange(currentCharacterset) {
      this.$http.get('/helper/collation/' + currentCharacterset).then(res => {
        this.collation = ''
        this.collationList = res
      }).catch(err => {
        console.error(err)
      })
    },
    createDatabase() {
      if (this.dbname === '' || this.characterset === '' || this.collation === '') {
        this.$message({
          showClose: true,
          message: '请将数据库信息填写完整',
          type: 'warning'
        })
      } else {
        this.$http.post('/new/database', {
          dbname: this.dbname,
          characterset: this.characterset,
          collation: this.collation
        }).then(res => {
          if (res.statusCode === 20000) {
            this.$confirm('数据库创建成功！', '提示', {
              confirmButtonText: '确定',
              type: 'success'
            }).then(() => {
              this.$router.push('/db/list')
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
      }
    }
  }
}
</script>

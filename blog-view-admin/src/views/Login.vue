<template>
  <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <h3 class="title">系统登录</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="ruleForm2.account" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @click.native.prevent="handleSubmit2" :loading="logining">登录</el-button>
      <!--<el-button type="primary" style="width:45%;" @click.native.prevent="register">公司入驻</el-button>-->
    </el-form-item>
  </el-form>
</template>

<script>
  import { requestLogin } from '../api/api';
  //import NProgress from 'nprogress'
  export default {
    data() {
      return {
        logining: false,
        ruleForm2: {
          account: 'admin',
          checkPass: '123456'
        },
        rules2: {
          account: [
            { required: true, message: '请输入账号', trigger: 'blur' },
            //{ validator: validaePass }
          ],
          checkPass: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            //{ validator: validaePass2 }
          ]
        },
        checked: true
      };
    },
    methods: {
      register(){
          //跳转到公司入驻界面
          this.$router.push({ path: '/register' });
      },
      handleReset2() {
        this.$refs.ruleForm2.resetFields();
      },
      //处理登录请求
      handleSubmit2(ev) {
        this.$refs.ruleForm2.validate((valid) => {
          if (valid) {
            this.logining = true;
            var loginParams = {
              userName: this.ruleForm2.account,
              password: this.ruleForm2.checkPass,
            };

            this.$router.push({ path: '/echarts' });
            this.logining = false;
            /*
            //发请求获取token
            this.$http.post("/login/login", loginParams).then(res=>{
              if(res.data.success){
                var userData = res.data.data;
                //把token存储起来
                // localStorage.setItem("U-TOKEN",userData.token);
                localStorage.setItem("realName",userData.realName);
                localStorage.setItem("userPic",userData.avatar);
                //修改登录成功后跳转到首页
                this.$router.push({ path: '/echarts' });
                this.logining = false;
                return;
              }else{
                this.logining = false;
                this.$message.error(res.data.message);
              }
            }).catch(error => {
              this.$message.error(error.message);
              this.logining = false;
              return;
            });
            */

          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    }
  }

</script>

<style lang="scss" scoped>
  .login-container {
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>

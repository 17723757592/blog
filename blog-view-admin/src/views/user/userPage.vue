<template>
  <div>

    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="queryParam.keyWord" placeholder="关键字" ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" v-on:click="doSearch">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="doAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表v-loading="listLoading"-->
    <el-table border :data="tableDatas" @selection-change="getSelection" v-loading="listLoading" highlight-current-row  style="width: 100%;">

      <el-table-column
          type="selection"
          width="40">
      </el-table-column>

      <el-table-column type="index" width="50" label="序号" fixed="left">
      </el-table-column>
      <el-table-column prop="realName" label="真实姓名" width="120" fixed="left">
      </el-table-column>
      <el-table-column prop="userName" label="登录账号" width="120">
      </el-table-column>
      <el-table-column prop="phonenumber" label="手机号" width="220" sortable>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="290">
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="200">
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template scope="scope">
          <el-button size="small"  @click="edit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="del(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" :disabled="delArray.length==0"  @click="doBatchDel">批量删除</el-button>
      <el-pagination
          @size-change="sizeChange"
          @current-change="pageChange"
          :current-page="queryParam.currentPage"
          :page-sizes="[5, 10, 50]"
          :page-size="queryParam.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalSize"
          style="float:right;">
      </el-pagination>
    </el-col>

    <!--新增界面-->
    <el-dialog :title="title" :visible.sync="addFormVisible"  :close-on-click-modal="false">
      <el-form :model="addForm" label-width="80px"  ref="addForm">
        <el-form-item label="账号" prop="userName">
          <el-input v-model="addForm.userName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="addForm.realName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="addForm.phonenumber" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="addForm.remark" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="addSubmit" >提交</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "userPage",
  data(){
    return{
      //定义表格需要的数据源
      tableDatas:[],
      //表格加载的进度条
      listLoading:false,
      //定义分页请求对象
      queryParam:{
        currentPage:1,
        pageSize:5,
        keyWord:''
      },
      //定义分页条的总条数
      totalSize:0,
      //定义批量删除的对象数组
      delArray:[],
      //定义弹框的开关
      addFormVisible:false,
      //定义表单数据
      addForm:{
        userName:'',
        realName:'',
        email:'',
        phonenumber:'',
        avatar:'',
        password:'',
        remark:'',
      },
      //定义对话框的标题
      title:""
    }
  },
  methods:{
    addSubmit(){
      this.$http.post('/user',this.addForm).then((res)=>{
        if (res.data.success){
          this.$message({
            message:'保存成功',
            type:'success'
          });
          //刷新
          this.loadUserData();
          //关闭窗口
          this.addFormVisible=false;
        }
      });
    }
    ,
    doAdd(){
      //打开添加对话框
      this.addFormVisible=true;
      //标题复制
      this.title="新增";
      //清空数据
      this.addForm={
        userName:'',
        realName:'',
        email:'',
        phonenumber:'',
        avatar:'',
        password:'',
        remark:'',
      }
    }
    ,
    doBatchDel(){
      //询问用户是否真的删除
      this.$confirm('确定删除这些数据吗？', '请确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        //发送请求
        this.$http.patch('/user',ids).then((res)=>{
          this.$message({
            message:'删除成功',
            type:'success'
          });
          this.loadUserData();
        });
      });
      //es6 数组Api投影对象属性 返回数组。
      let ids= this.delArray.map((obj)=>{return obj.id});


    },
    //监听表格选择事件，selectRow 表示当前选择的行
    getSelection( selectRow ){
      this.delArray=selectRow;
    },
    sizeChange(size){
      //获取新的页面大小
      this.queryParam.pageSize=size;
      //重新加载数据
      this.loadUserData();
    },
    doSearch(){
      //发送请求，根据keywrod关键字去进行分页查询
      this.loadUserData();
    },
    edit( row ){
      //打开弹窗
      this.addFormVisible=true;
      //修改标题
      this.title="编辑";
      //回显数据
      this.addForm= row;



    },
    del: function (row) {
      //获取Id
      let id=row.id;

      //询问用户是否真的删除
      this.$confirm('确定删除这些数据吗？', '请确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then( ()=>{
        //发送请求
        this.$http.delete('/user/'+id).then((res)=>{
          if (res.data.success) {
            this.$message({
              message:'删除成功',
              type:'success'
            });
            //重新加载
            this.loadUserData();
          }
        });
      }).catch( ()=>{
        this.$message('已取消')
      } );



    },
    pageChange( page ){
      //获取最新页面
      this.queryParam.currentPage=page;
      //重新添加数据
      this.loadUserData();
    },
    //加载用户数据
    loadUserData(){
      this.$http.post('/user/page',this.queryParam).then((res)=>{
        console.log(res.data.data.list);
        //把数据赋值给表格
        this.tableDatas=res.data.data.list;
        //把数据赋值给分页条
        this.totalSize=res.data.data.total;
      });
    }
  },
  //钩子函数发请求
  mounted() {
    this.loadUserData();
  }

}
</script>

<style scoped>

</style>
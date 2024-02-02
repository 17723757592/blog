<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :model="filters" :inline="true">
				<el-form-item>
					<el-input v-model="filters.keyword" placeholder="关键字" ></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="warning" v-on:click="getTableData">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="addHandler">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表v-loading="listLoading"-->
		<el-table border stripe :data="tableDatas" v-loading="listLoading" @selection-change="handleSelectionChange"
				  highlight-current-row  style="width: 100%;">
			<!--多选框-->
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="70" label="序号" fixed="left">
			</el-table-column>
			<el-table-column prop="name" label="链接名称" :show-overflow-tooltip="true" width="290" fixed="left">
			</el-table-column>
			<el-table-column prop="address" label="链接地址" :show-overflow-tooltip="true" width="420" fixed="left">
			</el-table-column>
			<el-table-column prop="status" label="状态" width="100">
				<template slot-scope="scope">
					<el-tag
							:type="scope.row.status == '0' ? 'danger' : 'success'"
							effect="light"
							disable-transitions>
						{{scope.row.status == '0' ? "不可用" : "可用"}}
					</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="realName" label="修改人" width="100">
			</el-table-column>
			<el-table-column prop="updateTime" label="修改时间" width="170" sortable>
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
			<el-button type="danger" :disabled="this.sels.length===0" @click="delBatch">批量删除</el-button>
			<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:current-page="page"
					:page-sizes="[5, 10, 50]"
					:page-size="rows"
					layout="total, sizes, prev, pager, next, jumper"
					:total="totalSize"
					style="float:right;">
			</el-pagination>
		</el-col>

		<!--新增界面-->
		<el-dialog title="新增友情链接" :visible.sync="addFormVisible"  :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px"  ref="addForm">
				<el-form-item label="链接名称" prop="name">
					<el-input v-model="addForm.name" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="链接地址" prop="address">
					<el-input v-model="addForm.address" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="状态" prop="status">
					<!--状态(0:不可用;1:可用)-->
					<el-radio-group v-model="addForm.status">
						<el-radio :label="1">可用</el-radio>
						<el-radio :label="0">不可用</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="addSubmit" >提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	export default {
		data() {
			return {
                row:"",
				sels: [], //存储多选记录
                addFormVisible:false,
				addForm:{
                    id:'',
					name:'',
					address:'',
					status:'',
				},
                listLoading:false,
				//查询对象
				filters:{
					keyword:''
				},
				totalSize: 0,//总记录条数
				page:1,//当前页,要传递到后台的
				rows:10, //每页显示多少条
			    tableDatas:[], //当前页数据
			}
		},
		methods: {
			onEditorReady(editor) {
				//当富文本编辑框初始化好执行
				console.log('富文本编辑框初始化---------完成');
			},

			//表格多条数据选中事件
			handleSelectionChange(val) {
				/**
				 * val中存放的是所有已选择的行数据集合
				 */
				this.sels = [];
				if(val != null && val.length > 0){
					for(var i=0; i<val.length; i++){
						//往sels数组中存值
						this.sels.push(val[i].id);
					}
				}
			},

			//提交新增表单数据
            addSubmit(){
                this.$http.post("/blogFriendAddress/save", this.addForm).then(result=>{
					if(result.data.success){
                        this.$message({
                            message: result.data.message,
                            type: 'success'
                        });
                        this.addFormVisible = false;
                        this.getTableData();
                    }else{
                        this.$message({
                            message: result.data.message,
                            type: 'error'
                        });
                    }
				});
			},
			//点击编辑
			edit(row){
				this.addFormVisible = true;//显示编辑框
				this.addForm = Object.assign({}, row);//对象赋值
			},
			//批量删除
			delBatch(){
				this.$confirm('确定删除这些数据吗？', '请确认', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					let param = {
						"ids": this.sels
					};
					console.log(param)
					this.$http.post("/blogFriendAddress/deleteBatch", param).then(result=>{
						if(result.data.success){
							this.$message({
								type: 'success',
								message: result.data.message
							});
							this.getTableData();
						}else{
							this.$message({
								type: 'error',
								message: result.data.message
							});
						}
					});
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消操作'
					});
				});
			},

			//删除单条数据
			del(row){
				this.$confirm('确定删除此条数据吗？', '请确认', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http.delete("/blogFriendAddress/" + row.userId).then(result=>{
						if(result.data.success){
							this.$message({
								type: 'success',
								message: result.data.message
							});
							this.getTableData();
						}else{
							this.$message({
								type: 'error',
								message: result.data.message
							});
						}
					});
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消操作'
					});
				});
			},
            addHandler(){
				this.addForm = {};
				this.addFormVisible = true;
				this.addForm.status = 1;
			},
			//选择第几页时触发
            handleCurrentChange(curentPage){
                this.page = curentPage;
                this.getTableData();
			},
			//选择每页显示记录条数
			handleSizeChange(curentPage){
				this.rows = curentPage
                this.getTableData();
			},
			handleChange(value) {
				console.log(this.addForm.courseTypeId);
			},
			//获取列表数据
            getTableData(){
				let para = {
				    "page": this.page,
				    "rows": this.rows,
					"keyword":this.filters.keyword
				};
				this.listLoading = true; //显示加载圈
				//分页查询
                this.$http.post("/blogFriendAddress/pagelist", para).then(result=>{
                        this.totalSize = result.data.data.total;
                        this.tableDatas = result.data.data.list;
                        this.listLoading = false;  //关闭加载圈
                    });
			},
		},
		mounted() {
		    this.getTableData();
		}
	}

</script>

<style scoped>

</style>

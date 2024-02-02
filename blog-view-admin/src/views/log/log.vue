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
			</el-form>
		</el-col>

		<!--列表v-loading="listLoading"-->
		<el-table border :data="tableDatas" v-loading="listLoading" @selection-change="handleSelectionChange"
				  highlight-current-row  style="width: 100%;">
			<!--多选框-->
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60" label="序号" fixed="left">
			</el-table-column>
			<el-table-column prop="content" label="日志内容" width="300">
			</el-table-column>
			<el-table-column prop="realName" label="操作人" width="80">
			</el-table-column>
			<el-table-column prop="createTime" label="操作时间" width="180" sortable>
			</el-table-column>
			<el-table-column label="操作" align="center" width="200" fixed="right">
				<template scope="scope">
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
					:page-sizes="[5, 10, 50, 100, 500, 1000]"
					:page-size="rows"
					layout="total, sizes, prev, pager, next, jumper"
					:total="totalSize"
					style="float:right;">
			</el-pagination>
		</el-col>

	</section>
</template>

<script>
    import { quillEditor } from "vue-quill-editor"; //调用编辑器
    import "quill/dist/quill.core.css"
	import "quill/dist/quill.snow.css"
	import "quill/dist/quill.bubble.css"

	export default {
        computed: {
			editor() {
                return this.$refs.myQuillEditor.quill
            }
		},
        components: {//使用编辑器
            quillEditor
        },
		data() {
			return {
                row:"",
				sels: [], //存储多选记录
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
					this.$http.post("/operLog/deleteBatch", param).then(result=>{
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
					this.$http.delete("/operLog/" + row.id).then(result=>{
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
            onEditorReady(editor) {
                //当富文本编辑框初始化好执行
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
                this.$http.post("/operLog/pagelist", para).then(result=>{
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

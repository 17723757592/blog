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
		<el-table border :data="tableDatas" v-loading="listLoading" @selection-change="handleSelectionChange"
				  highlight-current-row  style="width: 100%;">
			<!--多选框-->
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="70" label="序号" fixed="left">
			</el-table-column>
			<el-table-column prop="articleName" label="文章名称" :show-overflow-tooltip="true" width="390" fixed="left">
			</el-table-column>
			<el-table-column prop="articleTypeName" label="所属分类" :show-overflow-tooltip="true" width="160">
			</el-table-column>
			<el-table-column prop="articleTag" label="文章标签" :show-overflow-tooltip="true" width="300">
			</el-table-column>
			<el-table-column prop="realName" label="作者" width="110">
			</el-table-column>
			<el-table-column prop="articleReadCount" label="阅读量" width="110">
			</el-table-column>
			<el-table-column prop="articleState" label="文章状态" width="100">
				<template slot-scope="scope">
					<el-tag
							:type="scope.row.articleState == '0' ? 'success' : scope.row.articleState == '1' ? 'waring' : 'danger'"
							effect="light"
							disable-transitions>
						{{scope.row.articleState == '0' ? "草稿" : scope.row.articleState == '1' ? "发布" : "弃用"}}
					</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="topState" label="是否置顶" width="100">
				<!--是否置顶(0:不置顶;1:置顶)-->
				<template slot-scope="scope">
					<el-tag
							:type="scope.row.topState == '0' ? 'success' : 'danger'"
							effect="light"
							disable-transitions>
						{{scope.row.topState == '0' ? "不置顶" : "置顶"}}
					</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="updateTime" label="最后修改时间" width="170" sortable>
			</el-table-column>
			<el-table-column label="操作" align="center" width="170" fixed="right">
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
		<el-dialog title="新增文章" :visible.sync="addFormVisible"  :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px"  ref="addForm">
				<el-form-item label="文章类型" prop="articleType">
					<el-select v-model="addForm.articleType" placeholder="请选择文章类型">
						<el-option
								v-for="item in articleTypes"
								:key="item.id"
								:label="item.typeName"
								:value="item.id">
							<span style="float: left">{{ item.typeName }}</span>
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="文章名称" prop="articleName">
					<el-input v-model="addForm.articleName" placeholder="请输入文章名称" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="文章标签" prop="articleTag">
					<el-input v-model="addForm.articleTag" placeholder="请输入文章标签" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="文章状态" prop="articleState">
					<el-radio-group v-model="addForm.articleState">
						<el-radio :label="0">草稿</el-radio>
						<el-radio :label="1">发布</el-radio>
						<el-radio :label="2">弃用</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="是否置顶" prop="topState">
					<el-radio-group v-model="addForm.topState">
						<el-radio :label="0">不置顶</el-radio>
						<el-radio :label="1">置顶</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="文章简介" prop="articleRemark">
					<el-input type="textarea" :rows="4" v-model="addForm.articleRemark" placeholder="请输入文章简介" auto-complete="off"></el-input>
				</el-form-item>
				<el-form-item label="文章内容" prop="articleContent">
          <el-input
              type="textarea"
              :rows="6"
              placeholder="请输入文章内容"
              v-model="addForm.articleContent">
          </el-input>
<!--					<div class="edit_container">
						<quill-editor
								v-model="addForm.articleContent"
								ref="myQuillEditor"
								class="editer"
								:options="editorOption"
								@ready="onEditorReady($event)">
						</quill-editor>
					</div>-->
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
	import { quillEditor } from "vue-quill-editor"; //调用编辑器
	import "quill/dist/quill.core.css"
	import "quill/dist/quill.snow.css"
	import "quill/dist/quill.bubble.css"
	// 工具栏配置
	const toolbarOptions = [
		["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线 -----['bold', 'italic', 'underline', 'strike']
		["blockquote", "code-block"], // 引用  代码块-----['blockquote', 'code-block']
		[{ header: 1 }, { header: 2 }], // 1、2 级标题-----[{ header: 1 }, { header: 2 }]
		[{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表-----[{ list: 'ordered' }, { list: 'bullet' }]
		[{ script: "sub" }, { script: "super" }], // 上标/下标-----[{ script: 'sub' }, { script: 'super' }]
		[{ indent: "-1" }, { indent: "+1" }], // 缩进-----[{ indent: '-1' }, { indent: '+1' }]
		[{ direction: "rtl" }], // 文本方向-----[{'direction': 'rtl'}]
		[{ size: ["small", false, "large", "huge"] }], // 字体大小-----[{ size: ['small', false, 'large', 'huge'] }]
		[{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题-----[{ header: [1, 2, 3, 4, 5, 6, false] }]
		[{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色-----[{ color: [] }, { background: [] }]
		[{ font: [] }], // 字体种类-----[{ font: [] }]
		[{ align: [] }], // 对齐方式-----[{ align: [] }]
		["clean"], // 清除文本格式-----['clean']
		["image", "link"] // 链接、图片、视频-----['link', 'image', 'video']
	];

	export default {
		components: {
			//使用编辑器
			quillEditor
		},
		computed: {
			editor() {
				return this.$refs.myQuillEditor.quill
			},
		},
		data() {
			return {
                row:"",
				sels: [], //存储多选记录
				articleTypes: [], //课程类型
				//富文本编辑器配置
				editorOption: {
					modules: {
						//工具栏定义的
						toolbar: toolbarOptions
					},
					//主题
					theme: "snow",
					placeholder: "请开始书写您的博客内容..."
				},
                addFormVisible:false,
				addForm:{
                    id:'',
					articlePic:'',
					articleName:'',
					articleTag:'',
					articleRemark:'',
					articleState:'',
					articleContent:'',
					articleType:'',
					topState:'',
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
                this.$http.post("/blogArticle/save", this.addForm).then(result=>{
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
					this.$http.post("/blogArticle/deleteBatch", param).then(result=>{
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
					this.$http.delete("/blogArticle/" + row.userId).then(result=>{
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
			//点击新增
            addHandler(){
				this.addForm = {};
				this.addFormVisible = true;
				this.addForm.articleState = 1;
				this.addForm.topState = 0;
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
                this.$http.post("/blogArticle/pagelist", para).then(result=>{
                        this.totalSize = result.data.data.total;
                        this.tableDatas = result.data.data.list;
                        this.listLoading = false;  //关闭加载圈
                    });
			},
      //获取文章类型数据
            getArticleTypeData(){
                this.$http.get("/blogArticleType/list", null).then(result=>{
                        this.articleTypes = result.data.data;
                    });
			},
		},
		mounted() {
		    this.getTableData();
		    this.getArticleTypeData();
		}
	}

</script>

<style scoped>

</style>

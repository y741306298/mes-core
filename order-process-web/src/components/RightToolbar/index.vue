<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <!-- <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top" v-if="search">
        <el-button size="mini" circle icon="el-icon-search" @click="toggleSearch()" />
      </el-tooltip> -->
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button type="text" icon="el-icon-refresh-right" size="medium" @click="refresh()">刷新</el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="显隐列" placement="top" v-if="columns">
        <el-button type="text" icon="el-icon-s-operation" size="medium" @click="showColumn()">设置</el-button>
      </el-tooltip>
    </el-row>
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <!-- <el-transfer
        :titles="['显示', '隐藏']"
        v-model="value"
        :data="columns"
        @change="dataChange"
      ></el-transfer> -->
      <el-row>
        <el-col :span="10">
          <el-table ref="allTableRef" border :data="allColumnList" @selection-change="handleAllSelectionChange">
            <el-table-column label="序号" type="index" width="50" align="center" />
            <el-table-column type="selection" :selectable="isSelectTable" width="55" align="center" />
            <el-table-column label="名称[所有]" align="center" prop="label" />
          </el-table>
        </el-col>
        <el-col :span="3" style="text-align: center;">
          <div>
            <el-button type="primary" @click="clickUp" :disabled="selectCancelPropList.length != 1" class="mb10 ml10">上移</el-button>
            <el-button type="primary" @click="clickDown" :disabled="selectCancelPropList.length != 1" class="mb10">下移</el-button>
            <el-button type="primary" @click="showClick" :disabled="selectPropList.length == 0" icon="el-icon-arrow-right" class="mb10 mt20"></el-button>
            <el-button type="primary" @click="cancelShowClick" :disabled="selectCancelPropList.length == 0" icon="el-icon-arrow-left"></el-button>
          </div>
        </el-col>
        <el-col :span="10">
          <el-table ref="cancelTableRef" border :data="resultColumnList" @selection-change="handleCancelSelectionChange">
            <el-table-column label="序号" type="index" width="50" align="center" />
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="名称[显示]" align="center" prop="label" />
          </el-table>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveColumn">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: "RightToolbar",
    data() {
      return {
        // 显隐数据
        value: [],
        // 弹出层标题
        title: "显示/隐藏",
        // 是否显示弹出层
        open: false,
        allColumnList:[],
        resultColumnList:[],
        selectPropList: [],
        selectCancelPropList: [],
      };
    },
    props: {
      showSearch: {
        type: Boolean,
        default: true,
      },
      columns: {
        type: Array,
      },
      cacheKey: {
        type: String,
      },
      search: {
        type: Boolean,
        default: true,
      },
      gutter: {
        type: Number,
        default: 10,
      },
    },
    computed: {
      style() {
        const ret = {};
        if (this.gutter) {
          ret.marginRight = `${this.gutter / 2}px`;
        }
        return ret;
      }
    },
    watch:{
      columns:function(){
        this.initAllColumnList();
      },
      cacheKey: function(){
        this.initResultColumnList();

      }
    },
    created() {
      // 显隐列初始默认隐藏列
      for (let item in this.columns) {
        if (this.columns[item].visible === false) {
          this.value.push(parseInt(item));
        }
      }
      this.initAllColumnList();
      this.initResultColumnList();
      this.displayAllColumn();
    },
    methods: {
      isSelectTable(row, index){
        if(row.visible == false){
          return false;
        }
        return true;
      },
      //初始化全部列表
      initAllColumnList(){
        const cacheColumn = JSON.stringify(this.columns);
        if(cacheColumn){
          this.allColumnList = JSON.parse(cacheColumn)
        }

      },
      //初始化显示的列表
      initResultColumnList() {
        const cacheColumn = this.$cache.local.get(this.cacheKey);
        if(cacheColumn){
          this.resultColumnList = JSON.parse(cacheColumn)
          this.displayAllColumn();
        }
      },
      //修改全部已选择的禁用
      displayAllColumn(){
        const _this = this;
        if(_this.resultColumnList != null ){
          _this.allColumnList.forEach((allItem,allIndex) => {
            _this.resultColumnList.forEach((resItem,resIndex) => {
              if(resItem.prop == allItem.prop){
                _this.$set(allItem,'visible',false);
              }
            })
          })
        }else{
          _this.resultColumnList = _this.allColumnList;
        }
      },
      //保存
      saveColumn(){
        this.open = false;
        this.$cache.local.set(this.cacheKey, JSON.stringify(this.resultColumnList))
        location.reload();
      },
      //上移
      clickUp(){
        const _this = this;
        let index = this.resultColumnList.findIndex((item,i)=>{
          return _this.selectCancelPropList.indexOf(item.prop)+1
        })
        if(index > 0){
          const temp = this.resultColumnList[index - 1];
          this.resultColumnList.splice( index - 1 , 1 , this.resultColumnList[index] );
          this.resultColumnList.splice( index , 1 , temp);
        }
      },
      //下移
      clickDown(){
        const _this = this;
        let index = this.resultColumnList.findIndex((item,i)=>{
          return _this.selectCancelPropList.indexOf(item.prop)+1
        })
        if(index < this.resultColumnList.length -1){
         const temp = this.resultColumnList[index + 1];
         this.resultColumnList.splice( index + 1 , 1 , this.resultColumnList[index] );
         this.resultColumnList.splice( index , 1 , temp);
        }
      },
      //取消选中
      cancelShowClick(){
        const _this = this;
        this.allColumnList.forEach((item,index) => {
          let i = _this.selectCancelPropList.indexOf(item.prop)
          if(i>=0){
            _this.$set(item,'visible',true);
            _this.resultColumnList.splice(_this.resultColumnList.findIndex((o,j)=> o.prop == item.prop),1)
          }
        })
        this.$refs.cancelTableRef.clearSelection()
      },
      //显示
      showClick(){
        const _this = this;
        this.allColumnList.forEach((item,index) => {
          let i = _this.selectPropList.indexOf(item.prop)
          if(i>=0){
            const showItem = item;
            _this.resultColumnList.push(JSON.parse(JSON.stringify(showItem)));
            _this.$set(item,'visible',false);
          }
        })
        this.$refs.allTableRef.clearSelection()
      },
      //取消选中
      handleCancelSelectionChange(selection){
        this.selectCancelPropList = selection.map(item => item.prop)
      },
      //所有选中结果
      handleAllSelectionChange(selection){
        this.selectPropList = selection.map(item => item.prop)
      },
      // 搜索
      toggleSearch() {
        this.$emit("update:showSearch", !this.showSearch);
      },
      // 刷新
      refresh() {
        this.$emit("queryTable");
      },
      // 右侧列表元素变化
      dataChange(data) {
        for (let item in this.columns) {
          const key = this.columns[item].key;
          this.columns[item].visible = !data.includes(key);
        }
      },
      // 打开显隐列dialog
      showColumn() {
        this.open = true;
      },
    },
  };
</script>
<style lang="scss" scoped>
  ::v-deep .el-transfer__button {
    border-radius: 50%;
    padding: 12px;
    display: block;
    margin-left: 0px;
  }

  ::v-deep .el-transfer__button:first-child {
    margin-bottom: 10px;
  }
</style>

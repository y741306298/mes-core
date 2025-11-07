 <template>
   <div class="app-container device-management">
     <el-card class="table-card" shadow="never">
       <div slot="header" class="clearfix card-header">
         <span>生产设备管理</span>
         <div class="card-actions">
           <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增设备</el-button>
         </div>
       </div>
       <el-table ref="deviceTable" :data="deviceList" border highlight-current-row :current-row="selectedDevice"
         @current-change="handleCurrentChange" size="small">
         <el-table-column type="index" label="#" width="50" align="center" />
         <el-table-column prop="assetNumber" label="资产编号" min-width="160" show-overflow-tooltip />
         <el-table-column prop="deviceName" label="设备名称" min-width="160" show-overflow-tooltip />
         <el-table-column prop="model" label="设备型号" min-width="120" show-overflow-tooltip />
         <el-table-column prop="category" label="设备类别" min-width="120" show-overflow-tooltip />
         <el-table-column prop="currentStatus" label="运行状态" min-width="120">
           <template slot-scope="scope">
             <span class="status-indicator" :style="{ color: scope.row.statusColor || '#909399' }">●</span>
             <span>{{ scope.row.currentStatus }}</span>
           </template>
         </el-table-column>
         <el-table-column prop="operator" label="当前操作员" min-width="120" show-overflow-tooltip />
         <el-table-column label="操作" width="200" align="center" fixed="right">
           <template slot-scope="scope">
             <el-button type="text" size="mini" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
             <el-button type="text" size="mini" icon="el-icon-edit"
               @click="handleEdit(scope.row, scope.$index)">编辑</el-button>
             <el-button type="text" size="mini" icon="el-icon-delete"
               @click="handleDelete(scope.row, scope.$index)">删除</el-button>
           </template>
         </el-table-column>
       </el-table>
     </el-card>

     <div v-if="selectedDevice" class="detail-grid">
       <el-card class="detail-card" shadow="never">
         <div slot="header">基础信息</div>
         <el-descriptions :column="2" border size="small">
           <el-descriptions-item label="资产编号">{{ selectedDevice.assetNumber }}</el-descriptions-item>
           <el-descriptions-item label="设备名称">{{ selectedDevice.deviceName }}</el-descriptions-item>
           <el-descriptions-item label="设备型号">{{ selectedDevice.model }}</el-descriptions-item>
           <el-descriptions-item label="设备类别">{{ selectedDevice.category }}</el-descriptions-item>
           <el-descriptions-item label="品牌/制造商">{{ selectedDevice.brand }}</el-descriptions-item>
           <el-descriptions-item label="序列号">{{ selectedDevice.serialNumber }}</el-descriptions-item>
           <el-descriptions-item label="所属车间">{{ selectedDevice.workshop }}</el-descriptions-item>
           <el-descriptions-item label="位置">{{ selectedDevice.location }}</el-descriptions-item>
           <el-descriptions-item label="供应商">{{ selectedDevice.supplier }}</el-descriptions-item>
           <el-descriptions-item label="购买日期">{{ selectedDevice.purchaseDate }}</el-descriptions-item>
           <el-descriptions-item label="投入使用日期">{{ selectedDevice.startDate }}</el-descriptions-item>
           <el-descriptions-item label="资产原值">{{ selectedDevice.value }}</el-descriptions-item>
         </el-descriptions>
       </el-card>

       <el-card class="detail-card" shadow="never">
         <div slot="header">技术参数</div>
         <el-descriptions :column="2" border size="small">
           <el-descriptions-item label="额定功率">{{ selectedDevice.ratedPower }}</el-descriptions-item>
           <el-descriptions-item label="加工范围">{{ selectedDevice.machiningRange }}</el-descriptions-item>
           <el-descriptions-item label="工作电压">{{ selectedDevice.voltage }}</el-descriptions-item>
           <el-descriptions-item label="气压要求">{{ selectedDevice.airPressure }}</el-descriptions-item>
           <el-descriptions-item label="定位精度">{{ selectedDevice.positioningAccuracy }}</el-descriptions-item>
           <el-descriptions-item label="重复定位精度">{{ selectedDevice.repeatability }}</el-descriptions-item>
           <el-descriptions-item label="主轴转速">{{ selectedDevice.spindleSpeed }}</el-descriptions-item>
           <el-descriptions-item label="数控系统">{{ selectedDevice.cncSystem }}</el-descriptions-item>
         </el-descriptions>
       </el-card>

       <el-card class="detail-card" shadow="never">
         <div slot="header">运行状态</div>
         <el-descriptions :column="2" border size="small">
           <el-descriptions-item label="当前状态">
             <span class="status-indicator" :style="{ color: selectedDevice.statusColor || '#909399' }">●</span>
             <span class="status-text">{{ selectedDevice.currentStatus }}</span>
           </el-descriptions-item>
           <el-descriptions-item label="当前操作员">{{ selectedDevice.operator }}</el-descriptions-item>
           <el-descriptions-item label="当前生产任务">{{ selectedDevice.productionTask }}</el-descriptions-item>
           <el-descriptions-item label="班次">{{ selectedDevice.shift }}</el-descriptions-item>
           <el-descriptions-item label="累计运行时间">{{ selectedDevice.totalRuntime }}</el-descriptions-item>
           <el-descriptions-item label="本月运行">{{ selectedDevice.monthlyRuntime }}</el-descriptions-item>
         </el-descriptions>
       </el-card>

       <el-card class="detail-card" shadow="never">
         <div slot="header">维护保养</div>
         <el-descriptions :column="2" border size="small">
           <el-descriptions-item label="维护策略">{{ selectedDevice.maintenanceStrategy }}</el-descriptions-item>
           <el-descriptions-item label="保养周期">{{ selectedDevice.maintenanceCycle }}</el-descriptions-item>
           <el-descriptions-item label="上次保养">{{ selectedDevice.lastMaintenance }}</el-descriptions-item>
           <el-descriptions-item label="下次计划保养">{{ selectedDevice.nextMaintenance }}</el-descriptions-item>
           <el-descriptions-item label="保养内容">{{ selectedDevice.maintenanceContent }}</el-descriptions-item>
           <el-descriptions-item label="负责人">{{ selectedDevice.maintenanceOwner }}</el-descriptions-item>
         </el-descriptions>
       </el-card>

       <el-card class="detail-card" shadow="never">
         <div slot="header">效能分析 (OEE)</div>
         <el-descriptions :column="2" border size="small">
           <el-descriptions-item label="时间开动率">{{ selectedDevice.timeAvailability }}</el-descriptions-item>
           <el-descriptions-item label="性能开动率">{{ selectedDevice.performance }}</el-descriptions-item>
           <el-descriptions-item label="合格品率">{{ selectedDevice.qualityRate }}</el-descriptions-item>
           <el-descriptions-item label="OEE">{{ selectedDevice.oee }}</el-descriptions-item>
           <el-descriptions-item label="计划运行时间">{{ selectedDevice.plannedTime }}</el-descriptions-item>
           <el-descriptions-item label="实际运行">{{ selectedDevice.actualRuntime }}</el-descriptions-item>
           <el-descriptions-item label="故障停机">{{ selectedDevice.downtime }}</el-descriptions-item>
           <el-descriptions-item label="换模调试">{{ selectedDevice.changeover }}</el-descriptions-item>
           <el-descriptions-item label="本月产量">{{ selectedDevice.monthlyOutput }}</el-descriptions-item>
         </el-descriptions>
       </el-card>

       <el-card class="detail-card" shadow="never">
         <div slot="header">维修历史记录</div>
         <el-table :data="selectedDevice.history" border size="small" empty-text="暂无维修记录">
           <el-table-column prop="date" label="日期" width="120" />
           <el-table-column prop="symptom" label="故障现象" min-width="140" show-overflow-tooltip />
           <el-table-column prop="cause" label="原因分析" min-width="140" show-overflow-tooltip />
           <el-table-column prop="action" label="处理措施" min-width="160" show-overflow-tooltip />
           <el-table-column prop="duration" label="维修时长" width="120" />
           <el-table-column prop="person" label="维修人" width="100" />
         </el-table>
       </el-card>
     </div>
     <el-empty v-else description="请选择设备查看详情" />

     <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="960px" append-to-body>
       <el-form ref="deviceForm" :model="form" :rules="rules" label-width="140px">
         <el-tabs v-model="formActiveTab">
           <el-tab-pane label="基础信息" name="basic">
             <el-row :gutter="20">
               <el-col :span="12">
                 <el-form-item label="资产编号" prop="assetNumber">
                   <el-input v-model="form.assetNumber" placeholder="请输入资产编号" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="设备名称" prop="deviceName">
                   <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="设备型号" prop="model">
                   <el-input v-model="form.model" placeholder="请输入设备型号" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="设备类别" prop="category">
                   <el-input v-model="form.category" placeholder="请输入设备类别" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="品牌/制造商" prop="brand">
                   <el-input v-model="form.brand" placeholder="请输入品牌或制造商" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="序列号" prop="serialNumber">
                   <el-input v-model="form.serialNumber" placeholder="请输入序列号" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="所属车间" prop="workshop">
                   <el-input v-model="form.workshop" placeholder="请输入所属车间" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="位置" prop="location">
                   <el-input v-model="form.location" placeholder="请输入设备位置" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="供应商" prop="supplier">
                   <el-input v-model="form.supplier" placeholder="请输入供应商" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="购买日期" prop="purchaseDate">
                   <el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd"
                     placeholder="选择购买日期" style="width: 100%;" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="投入使用日期" prop="startDate">
                   <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" placeholder="选择投入使用日期"
                     style="width: 100%;" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="资产原值" prop="value">
                   <el-input v-model="form.value" placeholder="请输入资产原值" />
                 </el-form-item>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="技术参数" name="tech">
             <el-row :gutter="20">
               <el-col :span="12">
                 <el-form-item label="额定功率" prop="ratedPower">
                   <el-input v-model="form.ratedPower" placeholder="请输入额定功率" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="加工范围" prop="machiningRange">
                   <el-input v-model="form.machiningRange" placeholder="请输入加工范围" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="工作电压" prop="voltage">
                   <el-input v-model="form.voltage" placeholder="请输入工作电压" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="气压要求" prop="airPressure">
                   <el-input v-model="form.airPressure" placeholder="请输入气压要求" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="定位精度" prop="positioningAccuracy">
                   <el-input v-model="form.positioningAccuracy" placeholder="请输入定位精度" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="重复定位精度" prop="repeatability">
                   <el-input v-model="form.repeatability" placeholder="请输入重复定位精度" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="主轴转速" prop="spindleSpeed">
                   <el-input v-model="form.spindleSpeed" placeholder="请输入主轴转速" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="数控系统" prop="cncSystem">
                   <el-input v-model="form.cncSystem" placeholder="请输入数控系统" />
                 </el-form-item>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="运行状态" name="runtime">
             <el-row :gutter="20">
               <el-col :span="12">
                 <el-form-item label="当前状态" prop="currentStatus">
                   <el-input v-model="form.currentStatus" placeholder="请输入当前状态" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="状态颜色" prop="statusColor">
                   <el-color-picker v-model="form.statusColor" show-alpha :predefine="predefineColors" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="当前操作员" prop="operator">
                   <el-input v-model="form.operator" placeholder="请输入当前操作员" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="当前生产任务" prop="productionTask">
                   <el-input v-model="form.productionTask" placeholder="请输入当前生产任务" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="班次" prop="shift">
                   <el-input v-model="form.shift" placeholder="请输入班次" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="累计运行时间" prop="totalRuntime">
                   <el-input v-model="form.totalRuntime" placeholder="请输入累计运行时间" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="本月运行" prop="monthlyRuntime">
                   <el-input v-model="form.monthlyRuntime" placeholder="请输入本月运行时长" />
                 </el-form-item>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="维护保养" name="maintenance">
             <el-row :gutter="20">
               <el-col :span="12">
                 <el-form-item label="维护策略" prop="maintenanceStrategy">
                   <el-input v-model="form.maintenanceStrategy" placeholder="请输入维护策略" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="保养周期" prop="maintenanceCycle">
                   <el-input v-model="form.maintenanceCycle" placeholder="请输入保养周期" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="上次保养" prop="lastMaintenance">
                   <el-input v-model="form.lastMaintenance" placeholder="请输入上次保养信息" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="下次计划保养" prop="nextMaintenance">
                   <el-input v-model="form.nextMaintenance" placeholder="请输入下次计划保养" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="保养内容" prop="maintenanceContent">
                   <el-input v-model="form.maintenanceContent" placeholder="请输入保养内容" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="负责人" prop="maintenanceOwner">
                   <el-input v-model="form.maintenanceOwner" placeholder="请输入负责人" />
                 </el-form-item>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="效能分析" name="oee">
             <el-row :gutter="20">
               <el-col :span="12">
                 <el-form-item label="时间开动率" prop="timeAvailability">
                   <el-input v-model="form.timeAvailability" placeholder="请输入时间开动率" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="性能开动率" prop="performance">
                   <el-input v-model="form.performance" placeholder="请输入性能开动率" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="合格品率" prop="qualityRate">
                   <el-input v-model="form.qualityRate" placeholder="请输入合格品率" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="OEE" prop="oee">
                   <el-input v-model="form.oee" placeholder="请输入OEE" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="计划运行时间" prop="plannedTime">
                   <el-input v-model="form.plannedTime" placeholder="请输入计划运行时间" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="实际运行" prop="actualRuntime">
                   <el-input v-model="form.actualRuntime" placeholder="请输入实际运行时间" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="故障停机" prop="downtime">
                   <el-input v-model="form.downtime" placeholder="请输入故障停机时长" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="换模调试" prop="changeover">
                   <el-input v-model="form.changeover" placeholder="请输入换模调试时长" />
                 </el-form-item>
               </el-col>
               <el-col :span="12">
                 <el-form-item label="本月产量" prop="monthlyOutput">
                   <el-input v-model="form.monthlyOutput" placeholder="请输入本月产量" />
                 </el-form-item>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="维修记录" name="history">
             <div class="history-toolbar">
               <el-button type="primary" icon="el-icon-plus" size="mini" @click="addHistoryRow">新增记录</el-button>
             </div>
             <el-table :data="form.history" border size="small" empty-text="暂无维修记录" class="history-table">
               <el-table-column label="日期" width="150">
                 <template slot-scope="scope">
                   <el-date-picker v-model="scope.row.date" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"
                     size="mini" style="width: 100%;" />
                 </template>
               </el-table-column>
               <el-table-column label="故障现象" min-width="160">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.symptom" size="mini" placeholder="请输入故障现象" />
                 </template>
               </el-table-column>
               <el-table-column label="原因分析" min-width="160">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.cause" size="mini" placeholder="请输入原因分析" />
                 </template>
               </el-table-column>
               <el-table-column label="处理措施" min-width="180">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.action" size="mini" placeholder="请输入处理措施" />
                 </template>
               </el-table-column>
               <el-table-column label="维修时长" width="140">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.duration" size="mini" placeholder="请输入维修时长" />
                 </template>
               </el-table-column>
               <el-table-column label="维修人" width="120">
                 <template slot-scope="scope">
                   <el-input v-model="scope.row.person" size="mini" placeholder="请输入维修人" />
                 </template>
               </el-table-column>
               <el-table-column label="操作" width="80" align="center">
                 <template slot-scope="scope">
                   <el-button type="text" size="mini" icon="el-icon-delete"
                     @click="removeHistoryRow(scope.$index)">删除</el-button>
                 </template>
               </el-table-column>
             </el-table>
           </el-tab-pane>
         </el-tabs>
       </el-form>
       <div slot="footer" class="dialog-footer">
         <el-button type="primary" @click="submitForm">保 存</el-button>
         <el-button @click="dialogVisible = false">取 消</el-button>
       </div>
     </el-dialog>
   </div>
 </template>

 <script>
   const createEmptyDevice = () => ({
     assetNumber: '',
     deviceName: '',
     model: '',
     category: '',
     brand: '',
     serialNumber: '',
     workshop: '',
     location: '',
     supplier: '',
     purchaseDate: '',
     startDate: '',
     value: '',
     ratedPower: '',
     machiningRange: '',
     voltage: '',
     airPressure: '',
     positioningAccuracy: '',
     repeatability: '',
     spindleSpeed: '',
     cncSystem: '',
     currentStatus: '',
     statusColor: '#67C23A',
     operator: '',
     productionTask: '',
     shift: '',
     totalRuntime: '',
     monthlyRuntime: '',
     maintenanceStrategy: '',
     maintenanceCycle: '',
     lastMaintenance: '',
     nextMaintenance: '',
     maintenanceContent: '',
     maintenanceOwner: '',
     timeAvailability: '',
     performance: '',
     qualityRate: '',
     oee: '',
     plannedTime: '',
     actualRuntime: '',
     downtime: '',
     changeover: '',
     monthlyOutput: '',
     history: []
   })

   export default {
     name: 'ProductionDeviceManagement',
     data() {
       return {
         deviceList: [{
           assetNumber: 'PE-ASSET-2024-001',
           deviceName: '五轴立式加工中心',
           model: 'VM-850',
           category: '加工中心',
           brand: '发那科',
           serialNumber: 'FAN-8X5A-2024-001',
           workshop: '一车间A生产线',
           location: 'A区-05工位',
           supplier: '某某精密设备有限公司',
           purchaseDate: '2024-01-15',
           startDate: '2024-02-01',
           value: '¥1,850,000',
           ratedPower: '15 kW',
           machiningRange: 'X-800mm Y-500mm Z-450mm',
           voltage: '380V ±5%',
           airPressure: '0.7MPa',
           positioningAccuracy: '±0.005mm',
           repeatability: '±0.003mm',
           spindleSpeed: '50-12,000 rpm',
           cncSystem: '发那科 31i-B',
           currentStatus: '运行中',
           statusColor: '#67C23A',
           operator: '张三',
           productionTask: 'WO-20240520-008',
           shift: '白班',
           totalRuntime: '1,258 小时',
           monthlyRuntime: '186 小时',
           maintenanceStrategy: '按运行时长维护',
           maintenanceCycle: '每 500 运行小时',
           lastMaintenance: '2024-05-01 (1,000小时)',
           nextMaintenance: '2024-06-15 (1,500小时)',
           maintenanceContent: '更换主轴润滑油、清理导轨、检查刀具库',
           maintenanceOwner: '李四',
           timeAvailability: '92%',
           performance: '88%',
           qualityRate: '99%',
           oee: '80.3%',
           plannedTime: '720小时/月',
           actualRuntime: '662小时/月',
           downtime: '28小时',
           changeover: '30小时',
           monthlyOutput: '8,500件',
           history: [{
               date: '2024-05-10',
               symptom: '主轴异响',
               cause: '轴承磨损',
               action: '更换主轴轴承',
               duration: '4小时',
               person: '李四'
             },
             {
               date: '2024-04-05',
               symptom: '刀具库卡刀',
               cause: '传感器故障',
               action: '更换接近传感器',
               duration: '2小时',
               person: '王五'
             },
             {
               date: '2024-03-15',
               symptom: '冷却液泄漏',
               cause: '管路接头松动',
               action: '紧固接头，更换密封圈',
               duration: '1.5小时',
               person: '李四'
             }
           ]
         }],
         selectedDevice: null,
         selectedIndex: -1,
         dialogVisible: false,
         dialogTitle: '新增设备',
         formActiveTab: 'basic',
         form: createEmptyDevice(),
         editingIndex: -1,
         predefineColors: ['#67C23A', '#E6A23C', '#F56C6C', '#909399'],
         rules: {
           assetNumber: [{
             required: true,
             message: '请输入资产编号',
             trigger: 'blur'
           }],
           deviceName: [{
             required: true,
             message: '请输入设备名称',
             trigger: 'blur'
           }],
           model: [{
             required: true,
             message: '请输入设备型号',
             trigger: 'blur'
           }],
           category: [{
             required: true,
             message: '请输入设备类别',
             trigger: 'blur'
           }],
           brand: [{
             required: true,
             message: '请输入品牌/制造商',
             trigger: 'blur'
           }],
           serialNumber: [{
             required: true,
             message: '请输入序列号',
             trigger: 'blur'
           }],
           workshop: [{
             required: true,
             message: '请输入所属车间',
             trigger: 'blur'
           }],
           location: [{
             required: true,
             message: '请输入设备位置',
             trigger: 'blur'
           }],
           supplier: [{
             required: true,
             message: '请输入供应商',
             trigger: 'blur'
           }],
           purchaseDate: [{
             required: true,
             message: '请选择购买日期',
             trigger: 'change'
           }],
           startDate: [{
             required: true,
             message: '请选择投入使用日期',
             trigger: 'change'
           }],
           value: [{
             required: true,
             message: '请输入资产原值',
             trigger: 'blur'
           }],
           ratedPower: [{
             required: true,
             message: '请输入额定功率',
             trigger: 'blur'
           }],
           machiningRange: [{
             required: true,
             message: '请输入加工范围',
             trigger: 'blur'
           }],
           voltage: [{
             required: true,
             message: '请输入工作电压',
             trigger: 'blur'
           }],
           airPressure: [{
             required: true,
             message: '请输入气压要求',
             trigger: 'blur'
           }],
           positioningAccuracy: [{
             required: true,
             message: '请输入定位精度',
             trigger: 'blur'
           }],
           repeatability: [{
             required: true,
             message: '请输入重复定位精度',
             trigger: 'blur'
           }],
           spindleSpeed: [{
             required: true,
             message: '请输入主轴转速',
             trigger: 'blur'
           }],
           cncSystem: [{
             required: true,
             message: '请输入数控系统',
             trigger: 'blur'
           }],
           currentStatus: [{
             required: true,
             message: '请输入当前状态',
             trigger: 'blur'
           }]
         }
       }
     },
     created() {
       if (this.deviceList.length > 0) {
         this.selectedIndex = 0
         this.selectedDevice = this.deviceList[0]
       }
     },
     methods: {
       handleAdd() {
         this.dialogTitle = '新增设备'
         this.dialogVisible = true
         this.formActiveTab = 'basic'
         this.form = createEmptyDevice()
         this.editingIndex = -1
         this.$nextTick(() => {
           this.$refs.deviceForm && this.$refs.deviceForm.clearValidate()
         })
       },
       handleEdit(row, index) {
         this.dialogTitle = '编辑设备'
         this.dialogVisible = true
         this.formActiveTab = 'basic'
         this.form = JSON.parse(JSON.stringify(row))
         this.editingIndex = index
         this.$nextTick(() => {
           this.$refs.deviceForm && this.$refs.deviceForm.clearValidate()
         })
       },
       handleView(row) {
         this.selectedDevice = row
         this.selectedIndex = this.deviceList.indexOf(row)
         this.$nextTick(() => {
           this.$refs.deviceTable && this.$refs.deviceTable.setCurrentRow(row)
         })
       },
       handleDelete(row, index) {
         this.$confirm(`确认删除设备【${row.deviceName || row.assetNumber}】吗？`, '提示', {
             confirmButtonText: '确 定',
             cancelButtonText: '取 消',
             type: 'warning'
           })
           .then(() => {
             this.deviceList.splice(index, 1)
             if (this.deviceList.length === 0) {
               this.selectedDevice = null
               this.selectedIndex = -1
             } else if (index === this.selectedIndex) {
               const newIndex = index >= this.deviceList.length ? this.deviceList.length - 1 : index
               this.selectedIndex = newIndex
               this.selectedDevice = this.deviceList[newIndex]
               this.$nextTick(() => {
                 this.$refs.deviceTable && this.$refs.deviceTable.setCurrentRow(this.selectedDevice)
               })
             }
             this.$message.success('删除成功')
           })
           .catch(() => {})
       },
       handleCurrentChange(row) {
         if (!row) {
           this.selectedDevice = null
           this.selectedIndex = -1
           return
         }
         this.selectedDevice = row
         this.selectedIndex = this.deviceList.indexOf(row)
       },
       addHistoryRow() {
         this.form.history.push({
           date: '',
           symptom: '',
           cause: '',
           action: '',
           duration: '',
           person: ''
         })
       },
       removeHistoryRow(index) {
         this.form.history.splice(index, 1)
       },
       submitForm() {
         this.$refs.deviceForm.validate(valid => {
           if (!valid) {
             return
           }
           const payload = JSON.parse(JSON.stringify(this.form))
           if (!Array.isArray(payload.history)) {
             payload.history = []
           }
           if (this.editingIndex > -1) {
             this.$set(this.deviceList, this.editingIndex, payload)
             this.selectedIndex = this.editingIndex
           } else {
             this.deviceList.push(payload)
             this.selectedIndex = this.deviceList.length - 1
           }
           this.selectedDevice = this.deviceList[this.selectedIndex] || null
           this.dialogVisible = false
           this.$nextTick(() => {
             if (this.selectedDevice) {
               this.$refs.deviceTable && this.$refs.deviceTable.setCurrentRow(this.selectedDevice)
             }
           })
           this.$message.success('保存成功')
         })
       }
     }
   }
 </script>

 <style lang="scss" scoped>
   .device-management {
     .table-card {
       margin-bottom: 20px;
     }

     .card-header {
       display: flex;
       align-items: center;
       justify-content: space-between;
       font-weight: 600;
     }

     .card-actions {
       display: flex;
       gap: 8px;
     }

     .detail-grid {
       display: grid;
       grid-template-columns: repeat(auto-fit, minmax(360px, 1fr));
       grid-gap: 20px;
       margin-bottom: 20px;
     }

     .detail-card {
       .status-text {
         margin-left: 4px;
       }
     }

     .status-indicator {
       font-size: 16px;
       margin-right: 4px;
       vertical-align: middle;
     }

     .history-toolbar {
       margin-bottom: 10px;
       text-align: right;
     }

     .history-table ::v-deep .el-input__inner {
       padding: 0 10px;
     }
   }
 </style>

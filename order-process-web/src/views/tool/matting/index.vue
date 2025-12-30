<template>
  <div class="app-container">
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>SVG 抠图接口测试</span>
      </div>
      <el-form :model="mattingForm" label-width="120px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="原图路径">
              <el-input v-model="mattingForm.img_file_name" placeholder="例如：svg/test/yazi.png" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="SVG路径">
              <el-input v-model="mattingForm.svg_file_name" placeholder="例如：svg/test/4.svg" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="OSS标识">
              <el-input v-model="mattingForm.oss_code" placeholder="例如：photoai" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结果目录">
              <el-input v-model="mattingForm.result_dir" placeholder="例如：svg/temp" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="回调地址">
              <el-input v-model="mattingForm.callback_url" placeholder="http://xxx/svgMattingCallback" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否拆分">
              <el-switch v-model="mattingForm.is_split" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleMatting">调用 svgMatting</el-button>
        </el-form-item>
      </el-form>
      <div v-if="mattingResponse" class="response-block">
        <div class="response-title">请求结果</div>
        <pre class="response-body">{{ mattingResponse }}</pre>
      </div>
    </el-card>

    <el-card class="box-card" shadow="never" style="margin-top: 20px">
      <div slot="header" class="clearfix">
        <span>SVG 抠图裁剪接口测试</span>
      </div>
      <el-form :model="cuttingForm" label-width="120px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="原图路径">
              <el-input v-model="cuttingForm.img_file_name" placeholder="例如：svg/test/yazi.png" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="SVG路径">
              <el-input v-model="cuttingForm.svg_file_name" placeholder="例如：svg/test/yazi1.svg" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="OSS标识">
              <el-input v-model="cuttingForm.oss_code" placeholder="例如：photoai" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结果目录">
              <el-input v-model="cuttingForm.result_dir" placeholder="例如：svg/temp" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="回调地址">
              <el-input v-model="cuttingForm.callback_url" placeholder="http://xxx/svgMattingCuttingCallback" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否拆分">
              <el-switch v-model="cuttingForm.is_split" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="起始X">
              <el-input-number v-model="cuttingForm.start_x" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="起始Y">
              <el-input-number v-model="cuttingForm.start_y" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="结束X">
              <el-input-number v-model="cuttingForm.end_x" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="结束Y">
              <el-input-number v-model="cuttingForm.end_y" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleCutting">调用 svgMattingCutting</el-button>
        </el-form-item>
      </el-form>
      <div v-if="cuttingResponse" class="response-block">
        <div class="response-title">请求结果</div>
        <pre class="response-body">{{ cuttingResponse }}</pre>
      </div>
    </el-card>

    <el-card class="box-card" shadow="never" style="margin-top: 20px">
      <div slot="header" class="clearfix">
        <span>多边形排版接口测试</span>
      </div>
      <el-form :model="polygonNestForm" label-width="140px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请求ID">
              <el-input v-model="polygonNestForm.requestId" placeholder="不填则自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超时时长(秒)">
              <el-input-number
                v-model="polygonNestForm.timeoutSeconds"
                :min="0"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="材料列表 JSON">
          <el-input
            v-model="polygonNestForm.materialsText"
            type="textarea"
            :rows="6"
            placeholder='例如：[{"materialCode":"DEFAULT","materialName":"示例材料","widthMM":1000,"heightMM":1000}]'
          />
        </el-form-item>
        <el-form-item label="SVG列表 JSON">
          <el-input
            v-model="polygonNestForm.svgListText"
            type="textarea"
            :rows="8"
            placeholder='例如：[{"ossInfo":{"ossCode":"photoai","bucket":"order"},"fileId":"svg/test/4.svg","preResultCode":"","widthMM":100,"heightMM":100,"nestMethod":0,"extendInfo":""}]'
          />
        </el-form-item>
        <el-form-item label="回调地址">
          <el-input v-model="polygonNestForm.callbackUrl" placeholder="不填则使用默认回调" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handlePolygonNest">调用 polygonNest</el-button>
        </el-form-item>
      </el-form>
      <div v-if="polygonNestResponse" class="response-block">
        <div class="response-title">请求结果</div>
        <pre class="response-body">{{ polygonNestResponse }}</pre>
      </div>
    </el-card>

    <el-card class="box-card" shadow="never" style="margin-top: 20px">
      <div slot="header" class="clearfix">
        <span>PLT 切割接口测试</span>
      </div>
      <el-form :model="cutPltForm" label-width="140px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="OSS标识">
              <el-input v-model="cutPltForm.ossInfo.ossCode" placeholder="例如：photoai" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Bucket">
              <el-input v-model="cutPltForm.ossInfo.bucket" placeholder="例如：order" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="PLT 文件ID">
              <el-input v-model="cutPltForm.pltFileId" placeholder="文件ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="切割位置">
              <el-input-number
                v-model="cutPltForm.position"
                :min="0"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请求ID">
              <el-input v-model="cutPltForm.requestId" placeholder="不填则自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="回调地址">
              <el-input v-model="cutPltForm.callbackUrl" placeholder="不填则使用默认回调" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleCutPlt">调用 cutPlt</el-button>
        </el-form-item>
      </el-form>
      <div v-if="cutPltResponse" class="response-block">
        <div class="response-title">请求结果</div>
        <pre class="response-body">{{ cutPltResponse }}</pre>
      </div>
    </el-card>

    <el-card class="box-card" shadow="never" style="margin-top: 20px">
      <div slot="header" class="clearfix">
        <span>附加模板接口测试</span>
      </div>
      <el-form :model="appendTemplateForm" label-width="140px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请求ID">
              <el-input v-model="appendTemplateForm.requestId" placeholder="不填则自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="回调地址">
              <el-input v-model="appendTemplateForm.callbackUrl" placeholder="不填则使用默认回调" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="SVG 编码">
          <el-input v-model="appendTemplateForm.svgCode" type="textarea" :rows="4" placeholder="请输入 svgCode" />
        </el-form-item>
        <el-form-item label="模板编码">
          <el-input v-model="appendTemplateForm.templateCode" placeholder="请输入 templateCode" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleAppendTemplate">调用 appendTemplate</el-button>
        </el-form-item>
      </el-form>
      <div v-if="appendTemplateResponse" class="response-block">
        <div class="response-title">请求结果</div>
        <pre class="response-body">{{ appendTemplateResponse }}</pre>
      </div>
    </el-card>
  </div>
</template>

<script>
import { svgMatting, svgMattingCutting, polygonNest, cutPlt, appendTemplate } from '@/api/tool/matting'

export default {
  name: 'MattingTest',
  data() {
    return {
      loading: false,
      mattingForm: {
        img_file_name: 'svg/test/yazi.png',
        svg_file_name: 'svg/test/4.svg',
        oss_code: 'photoai',
        result_dir: 'svg/temp',
        callback_url: '',
        is_split: false
      },
      cuttingForm: {
        img_file_name: 'svg/test/yazi.png',
        svg_file_name: 'svg/test/yazi1.svg',
        oss_code: 'photoai',
        result_dir: 'svg/temp',
        callback_url: '',
        is_split: false,
        start_x: 500,
        start_y: 500,
        end_x: 1000,
        end_y: 1000
      },
      mattingResponse: '',
      cuttingResponse: '',
      polygonNestForm: {
        requestId: '',
        materialsText: JSON.stringify(
          [
            {
              materialCode: 'DEFAULT',
              materialName: '示例材料',
              widthMM: 1000,
              heightMM: 1000
            }
          ],
          null,
          2
        ),
        svgListText: JSON.stringify(
          [
            {
              ossInfo: {
                ossCode: 'photoai',
                bucket: 'order'
              },
              fileId: 'svg/test/4.svg',
              preResultCode: '',
              widthMM: 100,
              heightMM: 100,
              nestMethod: 0,
              extendInfo: ''
            }
          ],
          null,
          2
        ),
        timeoutSeconds: 120,
        callbackUrl: ''
      },
      cutPltForm: {
        ossInfo: {
          ossCode: 'photoai',
          bucket: 'order'
        },
        pltFileId: '',
        position: 0,
        callbackUrl: '',
        requestId: ''
      },
      appendTemplateForm: {
        requestId: '',
        svgCode: '',
        templateCode: '',
        callbackUrl: ''
      },
      polygonNestResponse: '',
      cutPltResponse: '',
      appendTemplateResponse: ''
    }
  },
  methods: {
    async handleMatting() {
      this.loading = true
      this.mattingResponse = ''
      try {
        const res = await svgMatting(this.mattingForm)
        this.mattingResponse = JSON.stringify(res, null, 2)
        this.$message.success('svgMatting 已触发，请等待回调')
      } catch (error) {
        this.mattingResponse = error ? error.toString() : '调用失败'
      } finally {
        this.loading = false
      }
    },
    async handleCutting() {
      this.loading = true
      this.cuttingResponse = ''
      try {
        const res = await svgMattingCutting(this.cuttingForm)
        this.cuttingResponse = JSON.stringify(res, null, 2)
        this.$message.success('svgMattingCutting 已触发，请等待回调')
      } catch (error) {
        this.cuttingResponse = error ? error.toString() : '调用失败'
      } finally {
        this.loading = false
      }
    },
    async handlePolygonNest() {
      this.loading = true
      this.polygonNestResponse = ''
      try {
        const { materialsText, svgListText, ...rest } = this.polygonNestForm
        const payload = {
          ...rest,
          materials: materialsText ? JSON.parse(materialsText) : [],
          svgList: svgListText ? JSON.parse(svgListText) : []
        }
        const res = await polygonNest(payload)
        this.polygonNestResponse = JSON.stringify(res, null, 2)
        this.$message.success('polygonNest 已触发，请等待回调')
      } catch (error) {
        this.polygonNestResponse = error ? error.toString() : '调用失败'
      } finally {
        this.loading = false
      }
    },
    async handleCutPlt() {
      this.loading = true
      this.cutPltResponse = ''
      try {
        const res = await cutPlt(this.cutPltForm)
        this.cutPltResponse = JSON.stringify(res, null, 2)
        this.$message.success('cutPlt 已触发，请等待回调')
      } catch (error) {
        this.cutPltResponse = error ? error.toString() : '调用失败'
      } finally {
        this.loading = false
      }
    },
    async handleAppendTemplate() {
      this.loading = true
      this.appendTemplateResponse = ''
      try {
        const res = await appendTemplate(this.appendTemplateForm)
        this.appendTemplateResponse = JSON.stringify(res, null, 2)
        this.$message.success('appendTemplate 已触发，请等待回调')
      } catch (error) {
        this.appendTemplateResponse = error ? error.toString() : '调用失败'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.response-block {
  margin-top: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  background: #fafafa;
}
.response-title {
  font-weight: 600;
  margin-bottom: 6px;
}
.response-body {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-all;
  font-family: Menlo, Monaco, Consolas, "Courier New", monospace;
}
</style>

<template>
  <el-submenu :index="menu.path">
    <template slot="title">{{ menu.meta.title }}</template>
    <template v-for="(child, index) in menu.children">
      <RecursiveSubMenu v-if="hasChildren(child)" :menu="child" :key="index"></RecursiveSubMenu>
      <el-menu-item v-else :index="child.path" :key="index">{{ child.meta.title }}</el-menu-item>
    </template>
  </el-submenu>
</template>


<script>
  export default {
    name: "RecursiveSubMenu",
    props: ['menu'],
    computed: {
      theme() {
        return this.$store.state.settings.theme;
      },
    },
    methods: {
      hasChildren(item) {
        return item.menuType === 'M';
      }
    }
  }
</script>

<style>
</style>

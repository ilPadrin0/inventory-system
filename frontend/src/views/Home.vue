<!-- src/views/Home.vue -->
<template>
  <div>
    <NavBar />
    <ProductList />
  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";
import ProductList from "../components/ProductList.vue";
export default {
  components: { NavBar, ProductList },
};
</script>

<style scoped>
/* 필요하면 여기에 스타일 추가 */
</style>

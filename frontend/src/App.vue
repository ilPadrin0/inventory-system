//App.vue
<template>
  <div>
    <ProductList
      @edit-product="handleEdit"
      @product-deleted="fetchProducts"
      @product-saved="fetchProducts"
      @update-options="updateOptions"
    />
  </div>
</template>

<script>
import ProductList from "./components/ProductList.vue";
import api from "./utils/axios";

export default {
  components: {
    ProductList,
  },
  data() {
    return {
      keyword: "",
      sortKey: "name",
      sortOrder: "asc",
      onlyAvailable: false,
    };
  },
  methods: {
    async fetchProducts() {
      try {
        const res = await api.get("/products/search", {
          params: {
            keyword: this.keyword,
            sortBy: this.sortKey,
            order: this.sortOrder,
            onlyAvailable: this.onlyAvailable,
          },
        });
        this.products = [...res.data];
      } catch (err) {
        console.error("상품 불러오기 실패:", err);
      }
    },
    handleEdit(product) {
      this.selectedProduct = product;
    },
    handleProductSaved() {
      this.fetchProducts();
      this.selectedProduct = null;
    },
    updateOptions(options) {
      if ("keyword" in options) this.keyword = options.keyword;
      if ("sortKey" in options) this.sortKey = options.sortKey;
      if ("sortOrder" in options) this.sortOrder = options.sortOrder;
      if ("onlyAvailable" in options)
        this.onlyAvailable = options.onlyAvailable;
      this.fetchProducts();
    },
  },
  mounted() {
    this.fetchProducts();
  },
};
</script>

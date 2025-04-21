<template>
  <div>
    <ProductForm
      :productToEdit="selectedProduct"
      @product-saved="handleProductSaved"
    />
    <ProductList
      :products="products"
      @edit-product="handleEdit"
      @product-deleted="fetchProducts"
    />
  </div>
</template>

<script>
import ProductForm from './components/ProductForm.vue';
import ProductList from './components/ProductList.vue';
import api from './utils/axios';

export default {
  components: {
    ProductForm,
    ProductList,
  },
  data() {
    return {
      products: [],
      selectedProduct: null,
    };
  },
  methods: {
    async fetchProducts() {
      console.log('상품 목록을 다시 불러옵니다...');
      const res = await api.get('/products');
      console.log('받아온 상품:', res.data);
      this.products = [...res.data];
    },
    handleEdit(product) {
      this.selectedProduct = product;
    },
    handleProductSaved() {
      this.fetchProducts();
      this.selectedProduct = null;
    },
  },
  mounted() {
    this.fetchProducts();
  },
};
</script>

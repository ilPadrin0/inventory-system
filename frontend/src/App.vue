<template>
  <div>
    <ProductForm :productToEdit="selectedProduct" @product-saved="fetchProducts" />
    <ProductList @edit-product="handleEdit" :products="products" />
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
      selectedProduct: null,
      products: [],
    };
  },
  methods: {
    async fetchProducts() {
      const res = await api.get('/products');
      this.products = res.data;
    },
    handleEdit(product) {
      this.selectedProduct = product;
    },
  },
  mounted() {
    this.fetchProducts();
  },
};
</script>

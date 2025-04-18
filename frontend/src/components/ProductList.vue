<template>
  <div>
    <h2>상품 목록</h2>
    <table>
      <thead>
        <tr>
          <th>상품명</th>
          <th>수량</th>
          <th>가격</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.name }}</td>
          <td>{{ product.quantity }}</td>
          <td>{{ product.price }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from '../utils/axios';

export default {
  name: 'ProductList',
  data() {
    return {
      products: [],
    };
  },
  mounted() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get('/api/products');
        this.products = response.data;
      } catch (error) {
        console.error('상품 목록 불러오기 실패:', error);
      }
    },
  },
};
</script>

<style scoped>
table {
  border-collapse: collapse;
  width: 100%;
  margin-top: 20px;
}
th, td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}
</style>

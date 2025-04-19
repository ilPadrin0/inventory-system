<template>
  <div>
    <h2>상품 목록</h2>
    <table>
      <thead>
        <tr>
          <th>상품명</th>
          <th>수량</th>
          <th>가격</th>
          <th>동작</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td v-if="editId !== product.id">{{ product.name }}</td>
          <td v-else><input v-model="editProduct.name" /></td>

          <td v-if="editId !== product.id">{{ product.quantity }}</td>
          <td v-else><input type="number" v-model="editProduct.quantity" /></td>

          <td v-if="editId !== product.id">{{ product.price }}</td>
          <td v-else><input type="number" v-model="editProduct.price" /></td>

          <td>
            <button v-if="editId !== product.id" @click="startEdit(product)">수정</button>
            <button v-else @click="updateProduct(product.id)">저장</button>
            <button v-if="editId === product.id" @click="cancelEdit">취소</button>
            <button @click="deleteProduct(product.id)">삭제</button>
          </td>
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
      editId: null,
      editProduct: {
        name: '',
        quantity: 0,
        price: 0,
      },
    };
  },
  mounted() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get('/products');
        this.products = response.data;
      } catch (error) {
        console.error('상품 목록 불러오기 실패:', error);
      }
    },
    async deleteProduct(id) {
      if (!confirm('정말 삭제하시겠습니까?')) return;

      try {
        await axios.delete(`/products/${id}`);
        this.fetchProducts(); // 삭제 후 목록 갱신
      } catch (error) {
        console.error('삭제 실패:', error);
      }
    },
    startEdit(product) {
      this.editId = product.id;
      this.editProduct = { ...product }; // 깊은 복사
    },
    cancelEdit() {
      this.editId = null;
      this.editProduct = { name: '', quantity: 0, price: 0 };
    },
    async updateProduct(id) {
      try {
        await axios.put(`/products/${id}`, this.editProduct);
        this.fetchProducts();
        this.cancelEdit();
      } catch (error) {
        console.error('수정 실패:', error);
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

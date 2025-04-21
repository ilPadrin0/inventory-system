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
  props: {
    products: Array,
  },
  data() {
    return {
      editId: null,
      editProduct: {
        name: '',
        quantity: 0,
        price: 0,
      },
    };
  },
  methods: {
    async deleteProduct(id) {
      if (!confirm('정말 삭제하시겠습니까?')) return;

      try {
        await axios.delete(`/products/${id}`);
        this.$emit('product-deleted');
      } catch (error) {
        console.error('삭제 실패:', error);
      }
    },
    startEdit(product) {
      this.editId = product.id;
      this.editProduct = { ...product };
    },
    cancelEdit() {
      this.editId = null;
      this.editProduct = { name: '', quantity: 0, price: 0 };
    },
    async updateProduct(id) {
      try {
        await axios.put(`/products/${id}`, this.editProduct);
        this.$emit('product-saved');
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

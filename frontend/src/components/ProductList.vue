<template>
  <div>
    <h2>상품 목록</h2>
    <input v-model="searchTerm" placeholder="상품명 검색" style="margin-bottom: 10px;" />
    <div style="margin: 10px 0;">
      <label>정렬 기준: </label>
      <select v-model="sortKey">
        <option value="name">상품명</option>
        <option value="price">가격</option>
        <option value="quantity">수량</option>
      </select>

      <label style="margin-left: 10px;">방식: </label>
      <select v-model="sortOrder">
        <option value="asc">오름차순</option>
        <option value="desc">내림차순</option>
      </select>
    </div>
    <div style="margin: 10px 0;">
      <label><input type="checkbox" v-model="onlyAvailable" /> 재고 있는 상품만 보기</label>
    </div>
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
        <tr v-for="product in filteredAndSortedProducts" :key="product.id">
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
      searchTerm: '',
      sortKey: 'name',
      sortOrder: 'asc',
      onlyAvailable: false,
    };
  },
  computed: {
    filteredAndSortedProducts() {
      let filtered = this.products.filter(product =>
        product.name.toLowerCase().includes(this.searchTerm.toLowerCase())
      );

      if (this.onlyAvailable) {
        filtered = filtered.filter(product => product.quantity > 0);
      }

      const sorted = [...filtered].sort((a, b) => {
        let aVal = a[this.sortKey];
        let bVal = b[this.sortKey];

        if (typeof aVal === 'string') {
          return this.sortOrder === 'asc'
            ? aVal.localeCompare(bVal)
            : bVal.localeCompare(aVal);
        }
        return this.sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
      });

      return sorted;
    },
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

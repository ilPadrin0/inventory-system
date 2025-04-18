<template>
  <div>
    <h2>상품 등록</h2>
    <form @submit.prevent="createProduct">
      <input v-model="name" placeholder="상품명" required />
      <input v-model.number="quantity" type="number" placeholder="수량" required />
      <input v-model.number="price" type="number" placeholder="가격" required />
      <button type="submit">등록</button>
    </form>
  </div>
</template>

<script>
import api from '../utils/axios';

export default {
  data() {
    return {
      name: '',
      quantity: 0,
      price: 0,
    };
  },
  methods: {
    async createProduct() {
      try {
        await api.post('/products', {
          name: this.name,
          quantity: this.quantity,
          price: this.price,
        });
        alert('상품이 등록되었습니다!');
        this.name = '';
        this.quantity = 0;
        this.price = 0;
      } catch (error) {
        console.error('등록 실패:', error);
      }
    },
  },
};
</script>

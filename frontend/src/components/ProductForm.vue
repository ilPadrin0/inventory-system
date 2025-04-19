<template>
  <div>
    <h2>{{ product.id ? '상품 수정' : '상품 등록' }}</h2>
    <form @submit.prevent="saveProduct">
      <input v-model="product.name" placeholder="상품명" required />
      <input v-model.number="product.quantity" type="number" placeholder="수량" required />
      <input v-model.number="product.price" type="number" placeholder="가격" required />
      <button type="submit">{{ product.id ? '수정' : '등록' }}</button>
    </form>
  </div>
</template>

<script>
import api from '../utils/axios';

export default {
  props: {
    productToEdit: Object, // 부모로부터 수정할 상품을 전달받음
  },
  data() {
    return {
      product: {
        id: null,
        name: '',
        quantity: 0,
        price: 0,
      },
    };
  },
  watch: {
    productToEdit: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.product = { ...newVal };
        }
      },
    },
  },
  methods: {
    async saveProduct() {
      try {
        if (this.product.id) {
          // 수정
          await api.put(`/products/${this.product.id}`, this.product);
          alert('상품이 수정되었습니다!');
        } else {
          // 등록
          await api.post('/products', this.product);
          alert('상품이 등록되었습니다!');
        }

        // 초기화
        this.product = { id: null, name: '', quantity: 0, price: 0 };
        this.$emit('product-saved'); // 부모에게 저장 완료 알림

      } catch (error) {
        console.error('저장 실패:', error);
      }
    },
  },
};
</script>

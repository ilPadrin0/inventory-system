<template>
  <div>
    <h2>{{ product.id ? '상품 수정' : '상품 등록' }}</h2>
    <form @submit.prevent="saveProduct">
      <input v-model="product.name" placeholder="상품명" />
      <input v-model.number="product.quantity" type="number" placeholder="수량" />
      <input v-model.number="product.price" type="number" placeholder="가격" />
      <button type="submit">{{ product.id ? '수정' : '등록' }}</button>
    </form>
  </div>
</template>

<script>
import api from '../utils/axios';

export default {
  props: {
    productToEdit: Object,
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
        if (newVal) this.product = { ...newVal };
      },
    },
  },
  methods: {
    async saveProduct() {
      if (!this.product.name.trim()) {
        alert("상품명을 입력해주세요.");
        return;
      }
      if (
        this.product.quantity === null ||
        this.product.quantity === '' ||
        isNaN(this.product.quantity) ||
        this.product.quantity < 0
      ) {
        alert("수량은 0 이상이어야 합니다.");
        return;
      }
      if (
        this.product.price === null ||
        this.product.price === '' ||
        isNaN(this.product.price) ||
        this.product.price <= 0
      ) {
        alert("가격은 1 이상이어야 합니다.");
        return;
      }

      try {
        if (this.product.id) {
          await api.put(`/products/${this.product.id}`, this.product);
          alert('상품이 수정되었습니다!');
        } else {
          await api.post('/products', this.product);
          alert('상품이 등록되었습니다!');
        }

        this.product = { id: null, name: '', quantity: 0, price: 0 };
        this.$emit('product-saved');

      } catch (error) {
        console.error('저장 실패:', error);
      }
    },
  },
};
</script>

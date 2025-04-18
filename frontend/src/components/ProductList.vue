<template>
  <div>
    <h1>상품 목록</h1>
    <ul>
      <li v-for="product in products" :key="product.id">
        {{ product.name }} - 수량: {{ product.quantity }}, 가격: {{ product.price }}원
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ProductList',
  data() {
    return {
      products: []
    }
  },
  mounted() {
    axios.get('http://localhost:8080/api/products')
      .then(response => {
        this.products = response.data
      })
      .catch(error => {
        console.error('상품 조회 실패:', error)
      })
  }
}
</script>

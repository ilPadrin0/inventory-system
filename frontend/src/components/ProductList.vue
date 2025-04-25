<template>
  <div>
    <StatisticsChart :statistics="statistics" />

    <h2>상품 목록</h2>
    <div class="toolbar">
      <input
        v-model="keyword"
        @input="fetchProducts"
        placeholder="상품명 검색"
        class="search"
      />

      <select v-model="sortKey" @change="fetchProducts">
        <option value="name">상품명</option>
        <option value="price">가격</option>
        <option value="quantity">수량</option>
      </select>

      <select v-model="sortOrder" @change="fetchProducts">
        <option value="asc">오름차순</option>
        <option value="desc">내림차순</option>
      </select>

      <label>
        <input
          type="checkbox"
          v-model="onlyAvailable"
          @change="fetchProducts"
        />
        재고 있는 상품만
      </label>
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
        <tr v-for="product in products" :key="product.id">
          <td v-if="editId !== product.id">{{ product.name }}</td>
          <td v-else><input v-model="editProduct.name" /></td>

          <td v-if="editId !== product.id">{{ product.quantity }}</td>
          <td v-else><input type="number" v-model="editProduct.quantity" /></td>

          <td v-if="editId !== product.id">{{ product.price }}</td>
          <td v-else><input type="number" v-model="editProduct.price" /></td>

          <td>
            <button v-if="editId !== product.id" @click="startEdit(product)">
              수정
            </button>
            <button v-else @click="updateProduct(product.id)">저장</button>
            <button v-if="editId === product.id" @click="cancelEdit">
              취소
            </button>
            <button @click="deleteProduct(product.id)">삭제</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "../utils/axios";
import { useToast } from "vue-toastification";
import StatisticsChart from "./StatisticsChart.vue";

export default {
  name: "ProductList",
  components: {
    StatisticsChart,
  },
  data() {
    return {
      products: [],
      keyword: "",
      sortKey: "name",
      sortOrder: "asc",
      onlyAvailable: false,
      editId: null,
      editProduct: { name: "", quantity: 0, price: 0 },
      toast: null,
      statistics: null,
    };
  },
  created() {
    this.toast = useToast();
    this.fetchProducts();
    this.fetchStatistics();
  },
  methods: {
    async fetchStatistics() {
      try {
        const res = await axios.get("/products/statistics");
        this.statistics = res.data;
      } catch (err) {
        console.error("통계 불러오기 실패:", err);
      }
    },
    async fetchProducts() {
      try {
        const res = await axios.get("/products/search", {
          params: {
            keyword: this.keyword,
            sortBy: this.sortKey,
            order: this.sortOrder,
            onlyAvailable: this.onlyAvailable,
          },
        });
        this.products = res.data;
      } catch (err) {
        console.error("상품 불러오기 실패:", err);
      }
    },
    async deleteProduct(id) {
      if (!confirm("정말 삭제하시겠습니까?")) return;
      try {
        await axios.delete(`/products/${id}`);
        this.fetchProducts();
        this.fetchStatistics();
        this.toast.success("상품이 삭제되었습니다!");
      } catch (error) {
        this.toast.error(error.response?.data || "삭제 실패!");
        console.error("삭제 실패:", error);
      }
    },
    async updateProduct(id) {
      const quantity = Number(this.editProduct.quantity);
      const price = Number(this.editProduct.price);

      if (!this.editProduct.name.trim()) {
        this.toast.error("상품명을 입력해주세요");
        return;
      }
      if (isNaN(quantity) || quantity < 0) {
        this.toast.error("수량은 0 이상이어야 합니다");
        return;
      }
      if (isNaN(price) || price < 0) {
        this.toast.error("가격은 0 이상이어야 합니다");
        return;
      }

      try {
        await axios.put(`/products/${id}`, {
          ...this.editProduct,
          quantity,
          price,
        });
        this.toast.success("상품이 수정되었습니다!");
        this.cancelEdit();
        this.fetchProducts();
        this.fetchStatistics();
      } catch (error) {
        this.toast.error(error.response?.data || "수정 실패!");
        console.error("수정 실패:", error);
      }
    },
    startEdit(product) {
      this.editId = product.id;
      this.editProduct = { ...product };
    },
    cancelEdit() {
      this.editId = null;
      this.editProduct = { name: "", quantity: 0, price: 0 };
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
th,
td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}
.toolbar {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
}
.toolbar input,
.toolbar select {
  padding: 5px;
  font-size: 14px;
}
.toolbar .search {
  flex: 1;
  min-width: 200px;
}
</style>

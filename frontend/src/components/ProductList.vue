//ProductList.vue
<template>
  <div>
    <StatisticsChart :statistics="statistics" />

    <div class="title-with-button">
      <h2>상품 목록</h2>
      <button @click="toggleCreateForm">
        {{ showCreateForm ? "닫기" : "상품 등록" }}
      </button>
    </div>

    <div v-if="showCreateForm" class="create-form">
      <input v-model="newProduct.name" placeholder="상품명" />
      <input
        v-model.number="newProduct.quantity"
        type="number"
        placeholder="수량"
      />
      <input
        v-model.number="newProduct.price"
        type="number"
        placeholder="가격"
      />
      <button @click="createProduct">저장</button>
    </div>

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
        <colgroup>
          <col style="width: 40%" />
          <col style="width: 15%" />
          <col style="width: 15%" />
          <col style="width: 30%" />
        </colgroup>
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
      showCreateForm: false,
      newProduct: { name: "", quantity: 0, price: 0 },
    };
  },
  created() {
    this.toast = useToast();
    this.fetchProducts();
    this.fetchStatistics();
  },
  methods: {
    toggleCreateForm() {
      this.showCreateForm = !this.showCreateForm;
      this.newProduct = { name: "", quantity: 0, price: 0 };
    },
    async createProduct() {
      const { name, quantity, price } = this.newProduct;

      if (!name.trim()) {
        this.toast.error("상품명을 입력해주세요");
        return;
      }
      if (!Number.isInteger(quantity) || quantity < 0) {
        this.toast.error("수량은 0 이상의 정수여야 합니다");
        return;
      }
      if (!Number.isInteger(price) || price < 1) {
        this.toast.error("가격은 1 이상의 정수여야 합니다");
        return;
      }

      try {
        await axios.post("/products", this.newProduct);
        this.toast.success("상품이 등록되었습니다!");
        this.fetchProducts();
        this.fetchStatistics();
        this.toggleCreateForm();
      } catch (error) {
        this.toast.error(error.response?.data || "등록 실패!");
        console.error("등록 실패:", error);
      }
    },
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
  max-width: 800px;
  margin: 20px auto;
  table-layout: fixed;
}

colgroup col:nth-child(1) { width: 40%; }
colgroup col:nth-child(2) { width: 15%; }
colgroup col:nth-child(3) { width: 15%; }
colgroup col:nth-child(4) { width: 30%; }

th,
td {
  border: 1px solid #ccc;
  padding: 6px 4px;
  text-align: center;
  font-size: 14px;
  vertical-align: middle;
  height: 40px;
}

td input {
  width: 100%;
  height: 30px;
  box-sizing: border-box;
  font-size: 14px;
  font-family: inherit;
  border: 1px solid #888;
  background-color: #2a2a2a;
  color: #fff;
  text-align: center;
  padding: 4px 6px;
  border-radius: 4px;
  outline: none;
  transition: all 0.2s;
}

td input:focus {
  border-color: #4fc3f7;
  background-color: #333;
}

.toolbar {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 15px;
  flex-wrap: wrap;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
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
.title-with-button {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-bottom: 10px;
}
.create-form {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}
.create-form input {
  padding: 5px;
  font-size: 14px;
}
</style>

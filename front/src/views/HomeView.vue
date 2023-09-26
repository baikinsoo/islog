<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter()

const posts = ref([]);

axios.get("/api/posts?page=1&size=5").then(response=>{
  response.data.forEach((r:any)=>{
    // 반응형이 아니기 data / value와 같이 설정을 해주지 않으면 데이터가 화면에 보이지 않는다.
    posts.value.push(r);
  })
});

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{name:'read',params:{postId:post.id}}">{{post.title}}</router-link>
      </div>

      <div class="content">
        {{post.content}}
      </div>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regdate">2023.09.26</div>
      </div>

    </li>
  </ul>
</template>

<style scoped lang="scss">
ul{
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
       font-size: 1.1rem;
        color: #383838;
       text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #7e7e7e;
    }

    &:last-child{
      margin-bottom: 0;
    }

    .sub{
      margin-top: 9px;
      font-size: 0.78rem;

      .regdate{
        margin-left: 10px;
        color: #6b6b6b
      }
    }
  }
}
</style>

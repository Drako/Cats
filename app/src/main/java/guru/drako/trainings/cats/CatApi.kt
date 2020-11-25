package guru.drako.trainings.cats

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

data class CatInfo(
  val url: String
)

data class BreedInfo(
  val id: String,
  val name: String
)

data class CategoryInfo(
  val id: Int,
  val name: String
)

class CatApi {
  private val client = HttpClient(Android) {
    install(JsonFeature) {
      serializer = GsonSerializer()
    }
  }

  suspend fun getCatInfos(limit: Int = 6, breedId: String? = null, categoryId: Int? = null): List<CatInfo> {
    return client
      .get("https://api.thecatapi.com/v1/images/search") {
        parameter("limit", limit)
        parameter("breed_ids", breedId)
        parameter("category_ids", categoryId)
      }
  }

  suspend fun getCatBreeds(): List<BreedInfo> {
    return client.get("https://api.thecatapi.com/v1/breeds")
  }

  suspend fun getCategories(): List<CategoryInfo> {
    return client.get("https://api.thecatapi.com/v1/categories")
  }
}

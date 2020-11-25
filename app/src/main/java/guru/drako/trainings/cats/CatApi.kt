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

typealias RequestConfigurator = HttpRequestBuilder.() -> Unit

class CatApi {
  private val client = HttpClient(Android) {
    install(JsonFeature) {
      serializer = GsonSerializer()
    }
  }

  companion object {
    private const val BASE_URL = "https://api.thecatapi.com/v1"

    private const val API_KEY = "cdb00885-1904-43a6-95f5-49a32d12a1f1"

    var cachedBreeds: List<BreedInfo> = listOf()
      private set
  }

  private suspend inline fun <reified T> get(path: String, block: RequestConfigurator = {}): T =
    client.get("$BASE_URL/$path") {
      header("x-api-key", API_KEY)
      block()
    }

  suspend fun getCatInfos(limit: Int = 6, breedId: String? = null, categoryId: Int? = null): List<CatInfo> =
    get("images/search") {
      parameter("limit", limit)
      parameter("breed_ids", breedId)
      parameter("category_ids", categoryId)
    }

  suspend fun getCatBreeds(): List<BreedInfo> {
    if (cachedBreeds.isEmpty()) {
      cachedBreeds = get("breeds")
    }
    return cachedBreeds
  }

  suspend fun getCategories(): List<CategoryInfo> = get("categories")
}

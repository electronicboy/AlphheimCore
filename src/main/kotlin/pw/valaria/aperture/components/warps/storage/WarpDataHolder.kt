package pw.valaria.aperture.components.warps.storage

import org.bukkit.Location
import java.util.concurrent.CompletableFuture

abstract class WarpDataHolder {

    abstract fun createWarp(name: String, target: Location, restricted: Boolean): CompletableFuture<TransactionResult>

    abstract fun deleteWarp(name: String): CompletableFuture<TransactionResult>

    sealed class TransactionResult {
        class Failure(message: String) : TransactionResult();
        object Success : TransactionResult()
    }

}

package shop.shopBE.domain.cartitem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartItem is a Querydsl query type for CartItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartItem extends EntityPathBase<CartItem> {

    private static final long serialVersionUID = -285426160L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartItem cartItem = new QCartItem("cartItem");

    public final shop.shopBE.domain.cart.entity.QCart cart;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> itemCount = createNumber("itemCount", Integer.class);

    public final NumberPath<Integer> itemPrice = createNumber("itemPrice", Integer.class);

    public final shop.shopBE.domain.product.entity.QProduct product;

    public QCartItem(String variable) {
        this(CartItem.class, forVariable(variable), INITS);
    }

    public QCartItem(Path<? extends CartItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartItem(PathMetadata metadata, PathInits inits) {
        this(CartItem.class, metadata, inits);
    }

    public QCartItem(Class<? extends CartItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new shop.shopBE.domain.cart.entity.QCart(forProperty("cart"), inits.get("cart")) : null;
        this.product = inits.isInitialized("product") ? new shop.shopBE.domain.product.entity.QProduct(forProperty("product")) : null;
    }

}


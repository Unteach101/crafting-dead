package com.craftingdead.core.capability.living;

public interface IPlayerHandler extends ILivingHandler {

  void copyFrom(IPlayer<?> that, boolean wasDeath);
}

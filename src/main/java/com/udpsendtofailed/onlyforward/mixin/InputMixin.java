package com.udpsendtofailed.onlyforward.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Input.class)
public class InputMixin {

    @Shadow public PlayerInput playerInput;
    @Shadow protected Vec2f movementVector;

    @Inject(method = "getMovementInput", at = @At("HEAD"))
    private void enforceTheRule(CallbackInfoReturnable<Vec2f> cir) {
        // 1. SANITIZE THE PHYSICS (The Vector)
        // Vec2f.x = Sideways (Strafing)
        // Vec2f.y = Forward (Positive) / Backward (Negative)
        
        float forward = this.movementVector.y;
        
        // If moving backward (Negative Y), stop dead.
        if (forward < 0.0F) {
            forward = 0.0F;
        }
        
        // Reconstruct the vector:
        // X = 0.0F (Kill A/D strafing)
        // Y = forward (Only allow W)
        this.movementVector = new Vec2f(0.0F, forward);

        // 2. SANITIZE THE INPUT STATE (The Server Packet)
        // Even if we fix the physics, we must stop the client from telling the server "I am pressing S".
        // PlayerInput is a Record: (forward, backward, left, right, jump, sneak, sprint)
        
        PlayerInput current = this.playerInput;
        
        this.playerInput = new PlayerInput(
            current.forward(), // W: Keep it.
            false,             // S: Burn it.
            false,             // A: Burn it.
            false,             // D: Burn it.
            current.jump(),    // Jump: Allowed.
            current.sneak(),   // Sneak: Allowed.
            current.sprint()   // Sprint: Allowed.
        );
    }
}
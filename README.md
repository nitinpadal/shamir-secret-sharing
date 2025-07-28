🔐 Goal:
Securely divide a secret into n shares so that any k (threshold) shares can reconstruct the original secret — but fewer than k cannot.

🧠 Logic in Simple Steps:
✅ 1. Represent the secret as a number
The secret (e.g., password, key) is first represented as a BigInteger.

For example:

BigInteger secret = new BigInteger("12345678901234567890");
✅ 2. Choose a prime modulus
All calculations are done modulo a large prime number to work within a finite field.

The prime must be larger than the secret.

This ensures modular arithmetic for security and correctness.

✅ 3. Create a random polynomial
Create a random polynomial of degree k-1, where:

k = threshold

k-1 = degree of polynomial

The secret is used as the constant term (a₀).

The rest of the coefficients (a₁, a₂, ..., aₖ₋₁) are random numbers.

Polynomial:
f(x) = a₀ + a₁x + a₂x² + ... + aₖ₋₁x⁽ᵏ⁻¹⁾ mod prime

✅ 4. Generate shares
For each participant i, compute the value of the polynomial at x = i:

y = f(i)
The share is (x, y).

Example:
Share 1 = (1, f(1)), Share 2 = (2, f(2)), ..., Share 5 = (5, f(5))

✅ 5. Distribute the shares
Send one share to each participant.

Even if someone loses their share, as long as k shares are available, the secret can be recovered.

✅ 6. Reconstruct the secret (Lagrange Interpolation)
Collect any k shares (x₁, y₁), ..., (xₖ, yₖ)

Use Lagrange interpolation to calculate f(0) (i.e., the constant term), which is the original secret.

Lagrange formula:
f(0) = Σ (yᵢ × Lᵢ(0)) mod prime,
where Lᵢ(0) is the Lagrange basis polynomial.

✅ 7. Return the recovered secret
After computing the interpolation result, return f(0) — this is the secret we started with.

📌 Why it works:
Polynomial of degree k-1 is uniquely determined by k points.

Any fewer points give no information about the secret — this is what makes SSS information-theoretically secure.

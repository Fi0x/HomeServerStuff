package io.github.fi0x.sailing.db;

import io.github.fi0x.sailing.db.entities.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrcCertificateRepo extends JpaRepository<CertificateEntity, String>
{
}

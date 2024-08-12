package codes.shiftmc.origins

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.RemoteRepository

@Suppress("UnstableApiUsage")
class OriginLoader : PluginLoader {

    override fun classloader(classpathBuilder: PluginClasspathBuilder) {
        val resolver = MavenLibraryResolver()
        resolver.addDependency(Dependency(DefaultArtifact(
            "com.github.ben-manes.caffeine:caffeine:3.1.8"
        ), null))
        resolver.addDependency(Dependency(DefaultArtifact(
            "org.jetbrains.kotlin:kotlin-stdlib:2.0.0"
        ), null))

        resolver.addRepository(
            RemoteRepository.Builder(
                "central",
                "default",
                "https://repo1.maven.org/maven2/"
            ).build()
        )

        classpathBuilder.addLibrary(resolver)
    }
}